package com.pottda.game.model.items;

import com.pottda.game.model.Item;

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

           x x o0
        [X]x
           x x o1
     */
    @Override
    protected void initDynamic() {
        basePositions.add(new int[]{0, 0});
        basePositions.add(new int[]{1, 1});
        basePositions.add(new int[]{1, 0});
        basePositions.add(new int[]{1, -1});

        baseOutputs.add(new int[]{2, 1});
    }
}
