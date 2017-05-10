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
    public final Map<Integer, Item> positionMap;

    private int height;
    private int width;

    /**
     * Called on creation and should be called whenever the
     * Inventory's state is changed (moved/changed items etc.)
     */
    public void compile() {
        // Map out all the positions of the items
        positionMap.clear();
        for (Item item : items) {
            for (Integer n : item.getPositionsAsIntegers(width)) {
                positionMap.put(n, item);
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
            for (int i = 0; i < outputs.size(); i++) {
                item.outputItems.add(i, positionMap.get(outputs.get(i)));
            }
        }
    }

    /**
     * Checks to see if the current inventory state is legal, i.e.
     * <p>
     * - no infinite loops are created
     *
     * @return true if the above conditions are fulfilled
     */
    public boolean isLegal() {
        Set<Item> checkedItems;

        for (Item i : this.attackItems) {
            checkedItems = new HashSet<Item>();
            if (isLooping(i, checkedItems)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Recursively controls all the output items of the given item
     * so that a single item does not appear more than once in the
     * chain.
     *
     * @param item         the Item to start with
     * @param checkedItems a {@link Set} of Items already in the chain
     * @return true if no loops are found
     */
    public boolean isLooping(Item item, Set<Item> checkedItems) {
        if (checkedItems.contains(item)) {
            return true;   // This means the item has already been visited once => illegal Inventory
        } else {
            for (Item i : item.outputItems) {    // Check all of the item's outputs in the same way
                if (i == null) {
                    continue;
                }
                if (!isLooping(i, checkedItems)) { // If any of them returns illegal, so is this etc.
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Attacks in the given direction
     *
     * @param direction a {@link Vector2f} in the wanted direction
     */
    public void attack(Vector2f direction, Vector2f origin) {

        // Iterate through all attack items and do stuff
        for (AttackItem a : attackItems) {
            if (a.isPrimaryAttack) {
                a.tryAttack(direction, origin);
            }
        }
    }


    public Inventory() {
        attackItems = new HashSet<AttackItem>();
        items = new HashSet<Item>();
        positionMap = new TreeMap<Integer, Item>();
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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
}
