package com.pottda.game.controller;

import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ViewActor;

import javax.vecmath.Vector2f;

public class DumbAIController extends AIController {
    public static ModelActor goal = null;

    public DumbAIController(ModelActor modelActor, ViewActor viewActor) {
        super(modelActor, viewActor);
    }

    @Override
    public void setInputVectors() {
        if (goal != null) {
            movementVector.set(goal.getPosition());
            movementVector.sub(modelActor.getPosition());

            if (movementVector.length() > 1) {
                movementVector.normalize();
            }
            attackVector.set(movementVector);
        }
    }
}
