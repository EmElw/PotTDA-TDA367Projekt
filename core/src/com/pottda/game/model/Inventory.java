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
    public final List<Item> items;
    /*
    A map kept to quickly find what Item is at a given position, if any
     */
    public final Map<Integer, Item> positionMap;

    private int height;
    private int width;

    // Should be called after creation and when the inventory's state is changed
    public void compile() {
        attackItems.clear();
        for (Item item : items) {
            if (item.isPrimaryAttack) {
                attackItems.add((AttackItem) item);
            }
            List<Integer> outputs = item.getOutputAsInteger(width);
            for (int i = 0; i < outputs.size(); i++) {
                item.outputItems.set(i, positionMap.get(outputs.get(i)));
            }
        }
        // TODO update stats
    }

    /**
     * Attacks in the given direction
     *
     * @param direction a {@link Vector2f} in the wanted direction
     */
    public void attack(Vector2f direction, Vector2f origin) {

        // Iterate through all attack items and do stuff
        for (AttackItem a : attackItems) {
            a.attack(direction, origin);
        }
    }

    public Inventory() {
        attackItems = new HashSet<AttackItem>();
        items = new ArrayList<Item>();
        positionMap = new TreeMap<Integer, Item>();
    }

    public void setDimensions(int w, int h) {
        this.width = w;
        this.height = h;
    }

    /**
     * Returns the sum of +maxHealth from items in the inventory
     */
    public int getHealth() {
        return 0;   // TODO implement inventory maxHealth
    }

    /**
     * Returns the sum of acceleration modifiers from items in the inventory
     */
    public float getAcceleration() {
        return 0;   // TODO implement inventory acceleration
    }
}
