package com.pottda.game.model.items;

import com.pottda.game.model.Item;
import com.pottda.game.model.Projectile;
import com.pottda.game.model.builders.ProjectileBuilder;
import com.sun.org.apache.xpath.internal.operations.Mult;

import javax.vecmath.Point2i;
import javax.vecmath.Vector2f;

/**
 * A "Nova" item:
 * <p>
 * It creates copies at in a fan-pattern
 */
public class Nova extends Item {
    private final static double RAD_SPREAD = 2*Math.PI;
    private final Vector2f temporaryVector = new Vector2f();

    private int additionalProjectiles;

    private boolean isFirstNova;

    /*
    Shaped like

        x
       [X]x o
      x x
     */
    @Override
    protected void initDynamic() {
        name = "Nova";

        isProjectileModifier = true;

        dropRate = 0.05f;

        basePositions.add(new Point2i(0, 0));
        basePositions.add(new Point2i(1, 0));
        basePositions.add(new Point2i(0, 1));
        basePositions.add(new Point2i(0, -1));
        basePositions.add(new Point2i(-1, 0));

        baseOutputs.add(new Point2i(2, 0));

        additionalProjectiles = 3;
    }

    @Override
    public void onAttack(Projectile p) {

        /*
        Nova needs to find all other Novaes and
        enchance its own effect

        Instead, it makes reduces the degrees between the shots.
         */

        int sumAdditionalProjectiles = 0;
        int count = 0;
        double totalSpreadRad = RAD_SPREAD;

        /*
         Get all the Nova listeners of the projectile
         and set them to be further be ignored. Sum the total
         number of additional projectiles.

         Needs to be ignored so that the created projectiles
         themselves don't create further copies
          */
        for (int i = 0; i < p.getListeners().size(); i++) {
            if (p.getListeners().get(i) instanceof Nova) {
                p.ignoreListener(i);
                sumAdditionalProjectiles += ((Nova) p.getListeners().get(i)).additionalProjectiles;
                count++;
            }
            if (p.getListeners().get(i) instanceof MultiShot) {
                p.ignoreListener(i);
                sumAdditionalProjectiles += 2;
            }
        }

        totalSpreadRad = totalSpreadRad/(sumAdditionalProjectiles + count);


        Vector2f position = p.getPosition();
        double direction = Math.toRadians(p.getAngle());

        /*
        Create thew new projectiles, set their appropriate direction
        and call their onAttack()
         */
        for (int i = 0; i < (sumAdditionalProjectiles+count); i++) {
            double newDir = direction - totalSpreadRad * (sumAdditionalProjectiles+count)
                    + totalSpreadRad * i;
            if (newDir >= direction) {
                newDir += totalSpreadRad;
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
