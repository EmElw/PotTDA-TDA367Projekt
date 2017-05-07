package com.pottda.game.model.items;

import com.pottda.game.model.Item;
import com.pottda.game.model.Projectile;

/**
 * A "MultiShot" item:
 * <p>
 * It creates copies at in a fan-pattern
 */
public class MultiShot extends Item {
    /*
    Shaped like

        x
       [X]x o
        x
     */
    @Override
    protected void initDynamic() {
        isProjectileModifier = true;

        unrotatedRelativePositions.add(new int[]{0, 0});
        unrotatedRelativePositions.add(new int[]{0, 1});
        unrotatedRelativePositions.add(new int[]{0, -1});
        unrotatedRelativePositions.add(new int[]{1, 0});

        unrotatedOutputPosition = new int[]{2, 0};
    }

    @Override
    public void onAttack(Projectile p) {
        /* TODO implement
        create projectiles next to p, with the
        same listeners excluding this one
        probably also reduce damage
        */
    }
}
