package com.pottda.game.model.items;

import com.pottda.game.model.Item;

/**
 * A "Switcher" item which toggles between output, meaning
 * that the item compilation takes a different path every
 * time
 */
public class Switcher extends Item {

    /*
    Shaped like
         0 1 2
      1    x o
      0  X x
     -1    x o
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
