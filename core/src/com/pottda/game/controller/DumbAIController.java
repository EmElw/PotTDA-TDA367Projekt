package com.pottda.game.controller;

import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ViewActor;

public class DumbAIController extends AIController {
    private static final int SAFE_DISTANCE = 4;
    static ModelActor goal = null;

    DumbAIController(ModelActor modelActor, ViewActor viewActor) {
        super(modelActor, viewActor);
    }

    @Override
    public void setInputVectors() {
        if (goal != null) {
            movementVector.set(goal.getPosition());
            movementVector.sub(modelActor.getPosition());

            attackVector.set(movementVector);
            attackVector.normalize();

            if (movementVector.length() < SAFE_DISTANCE) {
                movementVector.set(0, 0);
            } else {
                movementVector.normalize();
            }
        } else {
            movementVector.set(0, 0);
            attackVector.set(0, 0);
        }
    }
}
