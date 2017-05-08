package com.pottda.game.model.items;

import com.pottda.game.model.Item;
import com.pottda.game.model.Projectile;

/**
 * A "ChainAttack" item:
 * <p>
 * When a projectile is destroyed on hit, it
 * fires a new attack in the direction of the closest enemy
 */
public class ChainAttack extends Item {

    /*
    Shaped like

        x x x
       [X]  o
        x x x
     */
    @Override
    protected void initDynamic() {
        isProjectileModifier = true;
        isSecondaryAttackItem = true;

        basePositions.add(new int[]{0, 0});
        basePositions.add(new int[]{0, 1});
        basePositions.add(new int[]{0, -1});
        basePositions.add(new int[]{1, 1});
        basePositions.add(new int[]{1, -1});
        basePositions.add(new int[]{2, 1});
        basePositions.add(new int[]{2, -1});

        baseOutputs.add(new int[]{2, 0});

    }

    @Override
    public void onDestruction(Projectile p) {
        /* TODO implement ChainAttack
        Executes an attack much like an invetory in the direction
        of the closest enemy.
        */
    }
}
