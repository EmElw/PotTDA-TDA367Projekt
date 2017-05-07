package com.pottda.game.model;

import com.pottda.game.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rikar on 2017-04-07.
 */

public abstract class Item extends ProjectileListenerAdapter {
    /*
    An AttackItem is a starting point and cannot be chained into
     */
    protected boolean isAttackItem;
    /*
    A SecondaryAttackItem "stops" the chain, as items beyond it are linked to that item instead
     */
    protected boolean isSecondaryAttackItem;
    /*
    A ProjectileModifier modifies projectiles and needs to be attached as a ProjectileListener
     */
    protected boolean isProjectileModifier;
    /*
    A OneOff is an item that can only occur once per chain for whatever reason
     */
    protected boolean isOneOff;

    protected List<int[]> unrotatedRelativePositions; // A list of offsets for physical positions
    protected int[] unrotatedOutputPosition;          // A point where the item will look for its followup

    public int orientation;

    public int x;
    public int y;

    public void init() {
        unrotatedRelativePositions = new ArrayList<int[]>();
        // Set default properties
        isAttackItem = false;
        isProjectileModifier = false;
        isSecondaryAttackItem = false;

        // Set properties based on dynamic type
        initDynamic();
    }

    /**
     * Is called at the end of init(), meaning it overrides the
     * default properties if wanted
     */
    protected abstract void initDynamic();

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

        for (int[] p : unrotatedRelativePositions) {
            int[] rotatedPoint = Util.rotate(p[0], p[1], orientation);
            int v = (rotatedPoint[0] + x) +    // Add x to convert to absolute coordinate in Inventory
                    (rotatedPoint[1] + y) * w;     // Multiply to add the whole number of rows
            list.add(v);
        }


        //list.sort((Integer o1, Integer o2) -> (o1.compareTo(o2)));    // Does not work without API level 25
        Util.sortIntegerList(list, true);
        return list;
    }

    /**
     * Returns the output position of the {@code Item} as a single {@code Integer}.
     * <p>
     * The function sequences a coordinate grid with width {@code w} into a single number so
     * that i.e. (1,2) with w = 5 is equivalent to 1 + (2*5) = 11
     *
     * @param w
     * @return
     */
    public Integer getOutputAsInteger(int w) {
        int[] rotatedPoint = Util.rotate(
                unrotatedOutputPosition[0],
                unrotatedOutputPosition[1],
                orientation);

        return (rotatedPoint[0] + x) +
                (rotatedPoint[1] + y) * w;
    }
}
