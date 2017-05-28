package com.pottda.game.controller;

import com.pottda.game.model.Character;
import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ActorView;

import javax.vecmath.Vector2f;

/**
 * This Controller fires toward the player.
 * This Controller moves its actor toward a point that is chosen near the player. When that point has been reached a new
 * point is chosen.
 */
public class FixatingAIController extends AIController {
    private final static float MAX_FIXATION_POINT_DISTANCE_FROM_PLAYER = 4f;
    private Vector2f fixationPoint;
    private float lastDistanceToFixationPoint;

    FixatingAIController(ModelActor modelActor, ActorView actorView, EnemyHealthBarController enemyHealthBarController) {
        super(modelActor, actorView, enemyHealthBarController);
        fixationPoint = new Vector2f();
        lastDistanceToFixationPoint = 0f;
    }


    @Override
    protected void setInputVectors() {
        if (Character.getPlayer() != null && Character.getPlayer().getPosition() != null) {
            movementVector.set(getMovementVector());
            attackVector.set(Character.getPlayer().getPosition());
            attackVector.sub(modelActor.getPosition());
            attackVector.normalize();
        } else {
            movementVector.set(0, 0);
            attackVector.set(0, 0);
        }
    }

    private Vector2f getMovementVector() {
        Vector2f temp = new Vector2f(fixationPoint);
        temp.sub(modelActor.getPosition());
        float currentDistanceToFixationPoint = temp.length();

        if (currentDistanceToFixationPoint < 0
                || Math.abs(currentDistanceToFixationPoint - lastDistanceToFixationPoint) < 0.000001
                || lastDistanceToFixationPoint == 0f) {
            fixationPoint = getNewFixationPoint();
        }

        temp.set(fixationPoint);
        temp.sub(modelActor.getPosition());
        temp.normalize();

        return temp;
    }

    private Vector2f getNewFixationPoint() {
        float r = (float) Math.random() * MAX_FIXATION_POINT_DISTANCE_FROM_PLAYER;
        float rad = (float) (Math.random() * Math.PI * 2);

        Vector2f playerPosition = new Vector2f(Character.getPlayer().getPosition());
        Vector2f offset = new Vector2f((float) Math.cos(rad), (float) Math.sin(rad));
        offset.normalize();
        offset.scale(r);
        playerPosition.add(offset);

        return playerPosition;
    }
}
