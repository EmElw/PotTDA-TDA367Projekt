package com.pottda.game.model.items;

import com.pottda.game.model.AttackItem;

import javax.vecmath.Point2i;

public class EnemySimpleCannon extends AttackItem {
    @Override
    protected void initDynamic() {
        isPrimaryAttack = true;

        /*
        Shaped like

            [X]o
         */
        basePositions.add(new Point2i(0, 0));

        baseOutputs.add(new Point2i(1, 0));

        cooldownMS = 500;
        damage = 20;
    }
}
