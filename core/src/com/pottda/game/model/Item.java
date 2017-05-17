package com.pottda.game.model;

import com.pottda.game.Util;

import javax.vecmath.Point2i;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * A basic Item, heavily modifiable based on properties
 */
public abstract class Item extends ProjectileListenerAdapter {
    /**
     * An AttackItem is a starting point and cannot be chained into
     */
    protected boolean isPrimaryAttack;
    /**
     * Secondary attacks have an attack function but are not
     * directly called from the Inventory to attack. Rather,
     * these are triggered attacks that handle their own
     * attacks on certain events
     */
    protected boolean isSecondaryAttack;
    /**
     * A ProjectileModifier modifies projectiles and needs to be attached as a ProjectileListener
     */
    protected boolean isProjectileModifier;
    /**
     * List of points relative to origin 0,0 of the item's positions
     */
    protected List<Point2i> basePositions;
    /**
     * List of points where the item will look for its output paths
     * (Generally only one)
     */
    protected List<Point2i> baseOutputs;
    /**
     * A List to the items output items, generated by the Inventory when it compiles
     */
    protected List<Item> outputItems;
    protected Map<Stat, Double> statMap;
    /**
     * Direction of the item in terms of number pi/2 rotations
     */
    public int orientation;
    /**
     * X-orientation within its inventory
     */
    public int x;
    /**
     * Y-orientation within its inventory
     */
    public int y;
    /**
     * The name of the item
     */
    public String name;
    /**
     *
     */
    public String itemImageLocation;
    /**
     * Pseudo-constructor, called if instantiated without constructor (probably really bad practice)
     */
    public void init() {
        basePositions = new ArrayList<Point2i>();
        baseOutputs = new ArrayList<Point2i>();
        statMap = new EnumMap<Stat, Double>(Stat.class);
        outputItems = new ArrayList<Item>();
        // Set default properties
        isPrimaryAttack = false;
        isProjectileModifier = false;
        isSecondaryAttack = false;

        // Set properties based on dynamic type
        initDynamic();
    }

    /**
     * Returns a list of {@code Integer}, where each corresponds to a space where this item is.
     * <p>
     * The function sequences a coordinate grid with width {@code w} into a single number so
     * that i.e. (1,2) with w = 5 is equivalent to 1 + (2*5) = 11
     *
     * @param w the width of the grid
     * @return a {@code List<Integer>}
     */
    public List<Integer> getPositionsAsIntegers(int w) {
        List<Integer> list = new ArrayList<Integer>();

        for (Point2i p : basePositions) {
            Point2i rotatedPoint = Util.rotate(p.x, p.y, orientation);
            int v = (rotatedPoint.x + x) +    // Add x to convert to absolute coordinate in Inventory
                    (rotatedPoint.y + y) * w;     // Multiply to add the whole number of rows
            list.add(v);
        }

        return list;
    }

    /**
     * Returns a list of {@code Integer}, where each corresponds to a space where the item's
     * output is.
     * <p>
     * The function sequences a coordinate grid with width {@code w} into a single number so
     * that i.e. (1,2) with w = 5 is equivalent to 1 + (2*5) = 11
     *
     * @param w the width of the grid
     * @return a {@code List<Integer>}
     */
    public List<Integer> getOutputAsInteger(int w) {
        List<Integer> list = new ArrayList<Integer>();

        for (Point2i p : baseOutputs) {
            Point2i rotatedPoint = Util.rotate(p.x, p.y, orientation);
            int v = (rotatedPoint.x + x) +    // Add x to convert to absolute coordinate in Inventory
                    (rotatedPoint.y + y) * w;     // Multiply to add the whole number of rows
            list.add(v);
        }

        return list;
    }

    public double getStat(Stat stat) {
        return statMap.containsKey(stat) ? statMap.get(stat) : 0;
    }

    /**
     * Is called at the end of init(), meaning it overrides the
     * default properties if wanted
     */
    protected abstract void initDynamic();

    /**
     * Basic iteration implementation, can be changed to
     * accomodate different behaviours such as toggling
     * outputs, no output, conditional outputs etc.
     *
     * @return an {@code Item}
     */
    protected Item getNext() {
        return outputItems.get(0);
    }

    public List<Point2i> getBasePositions() {
        return basePositions;
    }

    public List<Point2i> getBaseOutputs() {
        return baseOutputs;
    }

    @Override
    public String toString() {
        return this.getClass().toString();
    }

}
