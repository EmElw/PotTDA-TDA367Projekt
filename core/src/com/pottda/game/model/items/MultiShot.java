package com.pottda.game.model.items;

import com.pottda.game.model.Item;
import com.pottda.game.model.Projectile;
import com.pottda.game.model.ProjectileListener;

import javax.vecmath.Point2i;

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
      x x
     */
    @Override
    protected void initDynamic() {
        isProjectileModifier = true;

        basePositions.add(new Point2i(0, 0));
        basePositions.add(new Point2i(1, 0));
        basePositions.add(new Point2i(0, 1));
        basePositions.add(new Point2i(0, -1));
        basePositions.add(new Point2i(-1, -1));

        baseOutputs.add(new Point2i(2,0));

        additionalProjectiles = 2;
        isFirstMultiShot = true;
    }

    private int additionalProjectiles;

    private boolean isFirstMultiShot;

    @Override
    public void onAttack(Projectile p) {

        /*
        MultiShot needs to find all other MultiShots and
        stop them from personally cloning the projectiles.

        Instead, it sums the additionalProjectiles to a
        common pool and multiplies the projectile based
        on that.
         */

        if (isProjectileModifier) {
            int sumProjectiles = 0;
            for (ProjectileListener item : p.projectileListeners) {
                if (item instanceof MultiShot) {
                    ((MultiShot) item).isFirstMultiShot = false;
                    sumProjectiles += ((MultiShot) item).additionalProjectiles;
                }
            }
            // TODO multiply projectiles
        } else {
            isFirstMultiShot = true;
        }
    }
}
