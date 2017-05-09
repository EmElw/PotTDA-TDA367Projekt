package com.pottda.game.controller;

import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ViewActor;

public class DumbAIController extends AIController{
    public final static float SPEED_MULTIPLIER = 200;
    public static ModelActor goal;

    public DumbAIController(ModelActor modelActor, ViewActor viewActor) {
        super(modelActor, viewActor);
    }

    @Override
    public void onNewFrame() {
        movementVector = goal.getPosition();
        movementVector.normalize();
        attackVector = movementVector;
        movementVector.set(movementVector.x * SPEED_MULTIPLIER, movementVector.y * SPEED_MULTIPLIER);

        super.onNewFrame();
    }
}
