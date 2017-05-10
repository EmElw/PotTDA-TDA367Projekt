package com.pottda.game.controller;

import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ViewActor;

import javax.vecmath.Vector2f;

public class DumbAIController extends AIController{
    public final static float SPEED_MULTIPLIER = 100;
    public static ModelActor goal;

    public DumbAIController(ModelActor modelActor, ViewActor viewActor) {
        super(modelActor, viewActor);
    }

    @Override
    public void onNewFrame() {
        final Vector2f goalPos = goal.getPosition();
        final Vector2f currentPos = modelActor.getPosition();

        movementVector = new Vector2f(goalPos.getX() - currentPos.getX(), goalPos.getY() - currentPos.getY());
        movementVector.normalize();
        attackVector = movementVector;
        attackVector.set(attackVector.x, attackVector.y);
        movementVector.set(movementVector.x * SPEED_MULTIPLIER, movementVector.y * SPEED_MULTIPLIER);

        super.onNewFrame();
    }
}
