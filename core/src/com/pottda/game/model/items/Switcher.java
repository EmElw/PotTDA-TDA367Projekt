package com.pottda.game.model.items;

import com.pottda.game.model.Item;

import javax.vecmath.Point2i;

/**
 * A "Switcher" item
 * <p>
 * It toggles between two output locations,
 * alternating between them whenever its
 * output is called
 */
public class Switcher extends Item {

    /*
    Shaped like

           o0
        [X]x
           o1
     */
    @Override
    protected void initDynamic() {

        basePositions.add(new Point2i(0, 0));
        basePositions.add(new Point2i(1, 0));

        baseOutputs.add(new Point2i(1, 1));
        baseOutputs.add(new Point2i(1, -1));

        state = false;
    }

    private boolean state;

    @Override
    protected Item getNext() {
        state = !state;
        return state ?
                outputItems.get(0) :
                outputItems.get(1);
    }
}