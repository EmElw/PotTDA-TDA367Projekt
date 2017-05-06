package com.pottda.game.model.items;

import com.pottda.game.model.AttackItem;
import com.pottda.game.model.ProjectileListener;

import javax.vecmath.Point2i;

/**
 * Created by Magnus on 2017-05-05.
 */
public class DemoItemB extends AttackItem {

    /*
    Shaped like

     xx
    xx

     */
    @Override
    public void init() {
        super.init();
        unrotatedRelativePositions.add(new int[]{0, 0});
        unrotatedRelativePositions.add(new int[]{1, 0});
        unrotatedRelativePositions.add(new int[]{1, 1});
        unrotatedRelativePositions.add(new int[]{2, 1});

        unrotatedOutputPosition = new int[]{3, 1};
    }
}
