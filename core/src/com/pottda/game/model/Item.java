package com.pottda.game.model;

import javax.vecmath.Point2i;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * A basic Item, heavily modifiable based on properties
 */
public abstract class Item extends ProjectileListenerAdapter {


    public Item() {
        init();
    }

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
    /**
     * The chance for an item to drop when the inventory containing it is destroyed
     * <p>
     * 1 = always dropped, 0 (default) = never dropped
     */
    protected float dropRate;

    protected String name;

    // -------------------------------------------------

    protected Map<Stat, Double> statMap;

    // Direction of the item in terms of number pi/2 rotations
    private int orientation;

    private int x;
    private int y;

    private boolean changedPositions = true;
    private boolean changedOutputs = true;

    private List<Point2i> rotatedOutputs;
    private List<Point2i> rotatedPositions;

    /**
     * Pseudo-constructor, called if instantiated without constructor (probably really bad practice)
     */
    public void init() {
        basePositions = new ArrayList<Point2i>();
        baseOutputs = new ArrayList<Point2i>();
        statMap = new EnumMap<Stat, Double>(Stat.class);
        outputItems = new ArrayList<Item>();

        changedPositions = true;
        changedOutputs = true;

        rotatedOutputs = new ArrayList<Point2i>();
        rotatedPositions = new ArrayList<Point2i>();

        // Set default properties
        isPrimaryAttack = false;
        isProjectileModifier = false;
        isSecondaryAttack = false;
        dropRate = 0;

        // Set properties based on dynamic type
        initDynamic();
    }

    double getStat(Stat stat) {
        return statMap.containsKey(stat) ? statMap.get(stat) : 0;
    }

    /**
     * Is called at the end of init(), meaning it overrides the
     * default properties if wanted
     */
    protected abstract void initDynamic();

    /**
     * Basic iteration implementation, can be changed to
     * accommodate different behaviours such as toggling
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

    public List<Point2i> getTransformedRotatedOutputs() {
        if (!changedOutputs) {
            return new ArrayList<Point2i>(rotatedOutputs);
        }

        rotatedOutputs = getTransformedRotatedList(baseOutputs);

        changedOutputs = false;

        return getTransformedRotatedOutputs();
    }

    /**
     * See {@see getTransformedRotatedOutputs()}
     *
     * @return a {@link List<Point2i>}
     */
    public List<Point2i> getTransformedRotatedPositions() {

        if (!changedPositions) {
            return new ArrayList<Point2i>(rotatedPositions);
        }

        rotatedPositions = getTransformedRotatedList(basePositions);

        changedPositions = false;

        return getTransformedRotatedPositions();
    }

    private List<Point2i> getTransformedRotatedList(List<Point2i> list) {
        List<Point2i> output = new ArrayList<Point2i>();

        for (Point2i p : list) {
            Point2i newPoint = rotate(p.x, p.y, orientation);
            newPoint.x += x;
            newPoint.y += y;
            output.add(newPoint);
        }
        return output;
    }


    /**
     * Call to randomly determine if this item should be dropped
     *
     * @param rateMultiplier a factor that alters the dropRate (values > 1 => increased chance)
     * @return true if the item should be dropped
     */
    boolean drop(float rateMultiplier) {
        return Math.random() < dropRate * rateMultiplier;
    }

    private Point2i bottomLeft;

    public Point2i getBaseBottomLeft() {
        if (bottomLeft != null) {
            return new Point2i(bottomLeft);
        }
        int lx = 0, ly = 0;
        for (Point2i p : baseOutputs) {
            lx = Math.min(p.x, lx);
            ly = Math.min(p.y, ly);
        }
        for (Point2i p : basePositions) {
            lx = Math.min(p.x, lx);
            ly = Math.min(p.y, ly);
        }

        bottomLeft = new Point2i(lx, ly);

        return getBaseBottomLeft();
    }

    private Point2i upperRight;

    public Point2i getBaseUpperRight() {
        if (upperRight != null) {
            return new Point2i(upperRight);
        }
        int hx = 0, hy = 0;
        for (Point2i p : baseOutputs) {
            hx = Math.max(p.x, hx);
            hy = Math.max(p.y, hy);
        }
        for (Point2i p : basePositions) {
            hx = Math.max(p.x, hx);
            hy = Math.max(p.y, hy);
        }

        upperRight = new Point2i(hx, hy);

        return getBaseUpperRight();
    }

    /**
     * Returns a copy of a rotated 2D point by n * pi/2 rad or n * 90 degrees around (0,0)
     * <p>
     * Home-brew rotation function because VecMath doesn't appear to support 2D matrices
     * (and it's sort of fast)
     *
     * @param x        the x-coordinate of the point
     * @param y        the y-coordinate of the point
     * @param rotation the rotation, expressed as n multiples of pi/2 rad
     * @return {@code int[]} of size 2
     */
    private static Point2i rotate(int x, int y, int rotation) {
        Point2i returnValue = new Point2i();
        rotation = rotation % 4;
        if (rotation < 0){
            rotation += 4;
        }
        returnValue.x = a[rotation] * x + b[rotation] * y;
        returnValue.y = c[rotation] * x + a[rotation] * y;
        return returnValue;
    }

    /**
     * Rotation matrix for r * pi/2
     * R = [ a[r] b[r] ; c[r] a[r]]
     */
    private static final int[] a = {1, 0, -1, 0};
    private static final int[] b = {0, -1, 0, 1};
    private static final int[] c = {0, 1, 0, -1};


    // Getters and setters

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
        changedOutputs = changedPositions = true;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        changedOutputs = changedPositions = true;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        changedOutputs = changedPositions = true;
    }

    public String getName() {
        return name;
    }
}
