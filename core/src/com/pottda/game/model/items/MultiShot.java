package com.pottda.game.model.items;

import com.pottda.game.model.Item;
import com.pottda.game.model.Projectile;
import com.pottda.game.model.builders.ProjectileBuilder;

import javax.vecmath.Point2i;
import javax.vecmath.Vector2f;

/**
 * A "MultiShot" item:
 * <p>
 * It creates copies at in a fan-pattern
 */
public class MultiShot extends Item {

    /*
    The spread between each projectile, expressed as radians?
     */
    private final static float SPREAD = 0.3f;
    private final Vector2f temporaryVector = new Vector2f();

    /*
    Shaped like

        x
       [X]x o
      x x
     */
    @Override
    protected void initDynamic() {
        name = "Multishot";

        isProjectileModifier = true;

        dropRate = 0.05f;

        basePositions.add(new Point2i(0, 0));
        basePositions.add(new Point2i(1, 0));
        basePositions.add(new Point2i(0, 1));
        basePositions.add(new Point2i(0, -1));
        basePositions.add(new Point2i(-1, -1));

        baseOutputs.add(new Point2i(2, 0));

        additionalProjectiles = 2;
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

        int sumAdditionalProjectiles = 0;

        /*
         Get all the MultiShot listeners of the projectile
         and set them to be further be ignored. Sum the total
         number of additional projectiles.

         Needs to be ignored so that the created projectiles
         themselves don't create further copies
          */
        for (int i = 0; i < p.getListeners().size(); i++) {
            if (p.getListeners().get(i) instanceof MultiShot) {
                p.ignoreListener(i);
                sumAdditionalProjectiles += ((MultiShot) p.getListeners().get(i)).additionalProjectiles;
            }
        }


        Vector2f position = p.getPosition();
        double direction = Math.toRadians(p.getAngle());

        /*
        Create thew new projectiles, set their appropiate direction
        and call their onAttack()
         */
        for (int i = 0; i < sumAdditionalProjectiles; i++) {
            double newDir = direction - SPREAD * sumAdditionalProjectiles / 2
                    + SPREAD * i;
            if (newDir >= direction) {
                newDir += SPREAD;
            }

            temporaryVector.set((float) Math.cos(newDir), (float) Math.sin(newDir));
            Projectile newProj = (Projectile) new ProjectileBuilder().
                    copyProperties(p).
                    setVelocity(temporaryVector).
                    setListeners(p.getListeners(), p.getIgnored()).
                    setPosition(position).
                    create();

            newProj.onAttack();
        }
    }
}
