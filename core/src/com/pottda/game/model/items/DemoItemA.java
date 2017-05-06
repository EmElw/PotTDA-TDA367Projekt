package com.pottda.game.model.items;

import com.pottda.game.model.AttackItem;
import com.pottda.game.model.ProjectileListener;

import javax.vecmath.Point2i;

/**
 * Created by Magnus on 2017-05-05.
 */
public class DemoItemA extends AttackItem {

    // Shaped like a square
    @Override
    public void initPositions() {
        isAttackItem = true;
        unrotatedRelativePositions.add(new int[]{0, 0});
        unrotatedRelativePositions.add(new int[]{1, 0});
        unrotatedRelativePositions.add(new int[]{0, 1});
        unrotatedRelativePositions.add(new int[]{1, 1});

        unrotatedOutputPosition = new int[]{2, 1};
    }

}
