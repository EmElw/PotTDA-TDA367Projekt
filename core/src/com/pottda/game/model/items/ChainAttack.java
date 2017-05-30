package com.pottda.game.model.items;

import com.pottda.game.model.AttackItem;
import com.pottda.game.model.Item;

import javax.vecmath.Point2i;

/**
 * A "ChainAttack" item:
 * <p>
 * When a projectile is destroyed on hit, it
 * fires a new attack in the direction of the closest enemy
 */
public class ChainAttack extends AttackItem {

    /*
    Shaped like

        x x x
        X[ ]o
        x x x
     */
    @Override
    protected void initDynamic() {
        name = "Chain";
        setColor(40,100,175);

        dropRate = 0.03f;

        isProjectileModifier = true;
        isSecondaryAttack = true;

        basePositions.add(new Point2i(-1, 1));
        basePositions.add(new Point2i(-1, 0));
        basePositions.add(new Point2i(-1, -1));
        basePositions.add(new Point2i(0, 1));
        basePositions.add(new Point2i(0, -1));
        basePositions.add(new Point2i(1, 1));
        basePositions.add(new Point2i(1, -1));

        baseOutputs.add(new Point2i(1, 0));

    }

    @Override
    public void onDestruction() {
        /* TODO implement ChainAttack
        Executes an attack much like an invetory in the direction
        of the closest enemy.
        */
    }

    @Override
    protected Item getNext() {
        return null;    // Is selfish
    }
}
