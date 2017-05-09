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
            if (item.isPrimaryAttack) { // Add attack items to a special subset list
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
     * Attacks in the given direction
     *
     * @param direction a {@link Vector2f} in the wanted direction
     */
    public void attack(Vector2f direction, Vector2f origin) {

        // Iterate through all attack items and do stuff
        for (AttackItem a : attackItems) {
            a.tryAttack(direction, origin);
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
