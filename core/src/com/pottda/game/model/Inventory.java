package com.pottda.game.model;

import javax.vecmath.Vector2f;
import java.util.*;

/**
 * An Inventory, containing {@link Item}.
 * <p>
 * Responsible for interpreting the properties and connections
 * of {@code Item}s as necessary
 */

public class Inventory {
    /*
    Starting points, set when compile() is called
     */
    public final Set<AttackItem> attackItems;
    /*
    A list of the Items in this Inventory
     */
    public final Set<Item> items;
    /*
    A map kept to quickly find what Item is at a given position, if any
     */
    private final Map<Integer, Item> positionMap;


    private List<InventoryChangeListener> inventoryChangeListeners;

    private int height;
    private int width;

    /*
    Set to true if two items attempts to set to the same coordinate.
    overlap = false is a condition for isLegal
     */
    private boolean overlap;

    /**
     * Initiate data structures
     */
    public Inventory() {
        overlap = false;
        attackItems = new HashSet<AttackItem>();
        items = new HashSet<Item>();
        positionMap = new TreeMap<Integer, Item>();
    }

    /**
     * Called on creation and should be called whenever the
     * Inventory's state is changed (moved/changed items etc.)
     */
    public void compile() {
        // Map out all the positions of the items
        positionMap.clear();
        overlap = false;
//        Item oldItem;
        for (Item item : items) {
            for (Integer n : item.getPositionsAsIntegers(width)) {
                if ((/*oldItem = */positionMap.put(n, item)) != null) {
                    overlap = true;
//                    throw new Exception("Illegal inventory: " + item + " overlaps with " + oldItem + " at " + n);
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
            List<Integer> outputs = item.getOutputAsInteger(width);
            item.outputItems.clear();
            for (Integer i : outputs) {
                item.outputItems.add(
                        positionMap.get(i));
            }
        }

        // Calls all the InventoryChangeListeners
        if (inventoryChangeListeners != null) {
            for (InventoryChangeListener icl : inventoryChangeListeners) {
                icl.inventoryChanged();
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
    public boolean isLegal() {

        // Check for overlapping items
        if (overlap) {
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
    public boolean isLooping(Item item, Set<Item> checkedItems) {
        if (checkedItems.contains(item)) {
            return true;        // Base fail case
        }

        checkedItems.add(item); // Add to checked items set

        // Recursively check each output
        for (int i = 0; i < item.outputItems.size(); i++) {
            Item op = item.outputItems.get(i);

            if (op == null) {
                return false;   // Base success case
            }
            if (isLooping(op,
                    i == item.outputItems.size() ?              // If it is the last output:
                            checkedItems :                      // - reuse the input set
                            new HashSet<Item>(checkedItems))) { // - otherwise, create a copy
                return true;
            }
        }
        return false;       // Reaches here if all outputs are not looping

    }

    /**
     * Attacks in the given direction
     *
     * @param direction a {@link Vector2f} in the wanted direction
     */
    public void attack(Vector2f direction, Vector2f origin, int team) {

        // Iterate through all attack items and do stuff
        for (AttackItem a : attackItems) {
            if (a.isPrimaryAttack) {
                a.tryAttack(direction, origin, team);
            }
        }
    }

    /**
     * Sets the dimensions of this {@code Inventory}
     *
     * @param w an integer width
     * @param h an integer height
     */
    public void setDimensions(int w, int h) {
        this.width = w;
        this.height = h;
    }

    /**
     * Returns the summed stat-value for the given stat
     * (not guaranteed to be positive)
     *
     * @param stat a {@link Stat}
     * @return a double
     */
    public double getSumStat(Stat stat) {

        double sum = 0;
        for (Item i : items) {
            sum += i.getStat(stat);
        }
        return sum;
    }

    /**
     * Adds any numbr of Items to the Inventory
     * NOTE that the item's position within the inventory is for the Item to handle
     *
     * @param items
     */
    public void addItem(Item... items) {
        this.items.addAll(Arrays.asList(items));
    }

    public Set<Item> getItemDropList() {
        return getItemDropList(1);
    }

    public Set<Item> getItemDropList(float dropRateFactor) {
        Set<Item> returnSet = new HashSet<Item>();
        for (Item i : items) {
            if (i.drop(dropRateFactor)) {
                returnSet.add(i);
            }
        }
        return returnSet;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setInventoryChangeListeners(List<InventoryChangeListener> inventoryChangeListeners) {
        this.inventoryChangeListeners = inventoryChangeListeners;
    }
}
