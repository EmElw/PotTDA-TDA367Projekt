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
        unrotatedRelativePositions.add(new int[]{0, 0});
        unrotatedRelativePositions.add(new int[]{1, 1});
        unrotatedRelativePositions.add(new int[]{1, 0});
        unrotatedRelativePositions.add(new int[]{1, -1});

        unrotatedOutputPosition = new int[]{2, 1};

    }

    /*
    Toggles between the two outputs whenever it is called
     */
    @Override
    public Integer getOutputAsInteger(int w) {
        unrotatedOutputPosition[1] *= -1;
        return super.getOutputAsInteger(w);
    }
}
