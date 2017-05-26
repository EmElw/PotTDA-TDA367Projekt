package com.pottda.game.model.items;

import com.pottda.game.model.AttackItem;

import javax.vecmath.Point2i;

public class PenetratingCannon extends AttackItem{
    @Override
    protected void initDynamic() {
        name = "Penetrating Cannon";

        isPrimaryAttack = true;

        piercing = true;

        /*
        Shaped like

             o
            [X]X
         */

        basePositions.add(new Point2i(0, 0));
        basePositions.add(new Point2i(1, 0));

        baseOutputs.add(new Point2i(0, 1));

        cooldownMS = 300;
        damage = 10;
    }
}
