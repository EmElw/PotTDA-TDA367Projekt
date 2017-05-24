package com.pottda.game.model.items;

import com.pottda.game.model.AttackItem;

import javax.vecmath.Point2i;

public class BouncingBallCannon extends AttackItem {

    @Override
    protected void initDynamic() {
        bounces = true;
        isPrimaryAttack = true;

        /*
        Shaped like

             X
            [X]o
         */

        basePositions.add(new Point2i(0, 0));
        basePositions.add(new Point2i(0, 1));

        baseOutputs.add(new Point2i(1, 0));

        cooldownMS = 300;
        damage = 10;
    }
}
