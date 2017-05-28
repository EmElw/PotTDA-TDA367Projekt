package com.pottda.game.model;

import javax.vecmath.Point2i;
import javax.vecmath.Vector2f;

import java.util.*;

/**
 * An Inventory, containing {@link Item}.
 * <p>
 * Responsible for interpreting the properties and connections
 * of {@code Item}s as necessary
 */

public class Inventory {
    public final Set<Item> items;
    private final List<InventoryChangeListener> inventoryChangeListeners;

    private int height;

    private int width;

    Inventory() {
        overlap = false;
        attackItems = new HashSet<AttackItem>();
        inventoryChangeListeners = new ArrayList<InventoryChangeListener>();
        items = new HashSet<Item>();
        positionMap = new TreeMap<Integer, Item>();
    }

    //Starting points, set when compile() is called
    final Set<AttackItem> attackItems;

    // Stores positions as (X + width * Y) integers, set in compile()
    private final Map<Integer, Item> positionMap;

    // Set to true in compile() if there are overlap between items
    private boolean overlap;
    // Set to true if an item's position is outside the item range ( p < 0 || p > width || p > height)
    private boolean outOfBounds;

    /**
     * Called on creation and should be called whenever the
     * Inventory's state is changed (moved/changed items etc.)
     * <p>
     * Sets the values of the attack items and position map
     */
    public void compile() {
        // Map out all the positions of the items
        positionMap.clear();
        overlap = false;
        for (Item item : items) {
            for (Point2i point : item.getTransformedRotatedPositions()) {
                if (point.x < 0 || point.y < 0) {
                    outOfBounds = true;
                }
                int n = point.x + point.y * width;
                if ((positionMap.put(n, item)) != null) {
                    overlap = true;
                }
            }
        }

        // Set the outputs of all items
        attackItems.clear();
        for (Item item : items) {
            if (item.isPrimaryAttack || item.isSecondaryAttack) {
                attackItems.add((AttackItem) item);
            }
            // Map each output to an Item or null
            item.outputItems.clear();
            List<Point2i> outputs = item.getTransformedRotatedOutputs();
            for (Point2i point : outputs) {
                int n = point.x + point.y * width;
                item.outputItems.add(positionMap.get(n));
            }
        }
    }

    /**
     * Checks to see if the current inventory state is legal, i.e.
     * <p>
     * - no infinite loops are created
     * <p>
     * - no items are overlapping
     *
     * @return true if the above conditions are fulfilled
     */
    boolean isLegal() {

        if (overlap || outOfBounds) {
            return false;
        }

        // Check for infinite loops (e.g. two items outputting to the other)
        for (Item i : this.attackItems) {
            if (isLooping(i, new HashSet<Item>())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Recursively controls all the output items of the given item
     * so that a single item does not appear more than once in the
     * chain.
     * <p>
     * In the case of branches, each branch is checked separately so
     * that an item can appear in both branches and still be legal
     *
     * @param item         the Item to start with
     * @param checkedItems a {@link Set} of Items already in the chain
     * @return true if no loops are found
     */
    private boolean isLooping(Item item, Set<Item> checkedItems) {
        if (checkedItems.contains(item)) {
            return true;        // Base fail case
        }

        checkedItems.add(item); // Add to checked items set

        // Recursively check each output
        for (Item op : item.outputItems) {
            if (op == null) {
                continue;
            }

            if (isLooping(op,
                    new HashSet<Item>(checkedItems))) {
                return true;
            }
        }
        return false;

    }

    public boolean itemLegalAt(int x, int y, int orientation, Item item) {

        // Does a mock-insert of the item and tries to validate
        // (No permanent change to the inventory state, no notifications)
        int oldX = item.getX(), oldY = item.getY(), oldOrientation = item.getOrientation();
        boolean inInventory = items.contains(item);


        item.setX(x);
        item.setY(y);
        item.setOrientation(orientation);
        if (!inInventory) {
            this.items.add(item);
        }

        compile();
        boolean legal = isLegal();

        item.setX(oldX);
        item.setY(oldY);
        item.setOrientation(oldOrientation);
        if (!inInventory) {
            this.items.remove(item);
        }

        compile();
        return true;
    }

    void attack(Vector2f direction, Vector2f origin, int team) {

        // Iterate through all attack items and do stuff
        for (AttackItem a : attackItems) {
            if (a.isPrimaryAttack) {
                a.tryAttack(direction, origin, team);
            }
        }
    }

    double getStatSum(Stat stat) {
        double sum = 0;
        for (Item i : items) {
            sum += i.getStat(stat);
        }
        return sum;
    }

    public Set<Item> getItemDropList() {
        return getItemDropList(1);
    }

    private Set<Item> getItemDropList(float dropRateFactor) {
        Set<Item> returnSet = new HashSet<Item>();
        for (Item i : items) {
            if (i.drop(dropRateFactor)) {
                returnSet.add(i);
            }
        }
        return returnSet;
    }

    // Standard stuff

    public void addInventoryChangeListener(InventoryChangeListener inventoryChangeListener) {
        this.inventoryChangeListeners.add(inventoryChangeListener);
    }

    public void removeInventoryChangeListener(InventoryChangeListener inventoryChangeListener) {
        this.inventoryChangeListeners.remove(inventoryChangeListener);
    }

    public void addItem(Item item) {
        items.add(item);
        compile();
        notifyListeners();
    }

    /**
     * Add items and update inventory + call listeners
     *
     * @param items a {@link Collection<Item>}
     */
    public void addItems(Collection<Item> items) {
        this.items.addAll(items);
        compile();
        notifyListeners();
    }

    public void removeItem(Item item) {
        items.remove(item);
        compile();
        notifyListeners();
    }

    public Set<Item> getItems() {
        return items;
    }

    public Item itemAt(Point2i point) {
        if (point.x >= 0 && point.x < width && point.y >= 0 && point.y < height) {
            return positionMap.get(point.x + point.y * width);
        } else {
            return null;
        }
    }

    void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private void notifyListeners() {
        // Calls all the InventoryChangeListeners
        for (InventoryChangeListener icl : inventoryChangeListeners) {
            icl.inventoryChanged();
        }
    }

    public void moveItem(Item item, int x, int y, int orientation) {
        if (items.contains(item)) {
            item.setX(x);
            item.setY(y);
            item.setOrientation(orientation);
            compile();
            notifyListeners();
        } else throw new Error("trying to move item outside this inventory");
    }
}
