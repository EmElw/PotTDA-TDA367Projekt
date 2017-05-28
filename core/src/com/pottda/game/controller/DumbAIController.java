package com.pottda.game.controller;

import com.pottda.game.model.Character;
import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ActorView;


/**
 * Basic AI controller that always wants to move towards its goal
 * and continuously attacks in that direction.
 * <p>
 * It stops moving towards its goal when within a set distance but
 * will remain attacking
 */
class DumbAIController extends AIController {
    private static final int DEFAULT_SAFE_DISTANCE = 4;
    private final int localSafeDistance;


    /**
     * {@inheritDoc}
     *
     * @param modelActor
     * @param actorView
     */
    DumbAIController(ModelActor modelActor, ActorView actorView, EnemyHealthBarController enemyHealthBarController) {
        super(modelActor, actorView, enemyHealthBarController);
        localSafeDistance = DEFAULT_SAFE_DISTANCE;
    }

    /**
     * Sets the safeDistance to another value
     *
     * @param modelActor   {@inheritDoc}
     * @param actorView    {@inheritDoc}
     * @param safeDistance an integer in meters
     */
    DumbAIController(ModelActor modelActor, ActorView actorView, EnemyHealthBarController enemyHealthBarController, int safeDistance) {
        super(modelActor, actorView, enemyHealthBarController);
        this.localSafeDistance = safeDistance;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setInputVectors() {
        if (Character.getPlayer() != null && Character.getPlayer().getPosition() != null) {
            movementVector.set(Character.getPlayer().getPosition());
            movementVector.sub(modelActor.getPosition());

            attackVector.set(movementVector);
            if (attackVector.length() != 0) {
                attackVector.normalize();
            }

            if (movementVector.length() < localSafeDistance) {
                movementVector.set(0, 0);
            }
            {
                if (movementVector.length() > 1) {
                    movementVector.normalize();
                }
            }
        } else {
            movementVector.set(0, 0);
            attackVector.set(0, 0);
        }
    }
}
