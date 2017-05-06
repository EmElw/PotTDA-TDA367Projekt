package com.pottda.game.model.items;

import com.pottda.game.model.Item;

/**
 * Created by Magnus on 2017-05-06.
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
    protected void initPositions() {
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
