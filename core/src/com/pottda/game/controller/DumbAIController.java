package com.pottda.game.controller;

import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ViewActor;

import javax.vecmath.Vector2f;

public class DumbAIController extends AIController{
    public static ModelActor goal;

    public DumbAIController(ModelActor modelActor, ViewActor viewActor) {
        super(modelActor, viewActor);
    }

    @Override
    public void onNewFrame() {
        final Vector2f goalPos = goal.getPosition();
        final Vector2f currentPos = modelActor.getPosition();

        movementVector = new Vector2f(goalPos.getX() - currentPos.getX(), goalPos.getY() - currentPos.getY());

        if (movementVector.length() < 3) {
            movementVector.set(0, 0);
        } else {
            movementVector.normalize();
//            movementVector.set(movementVector.x * 5f, movementVector.y * 5f);
        }

        if (movementVector.length() != 0) { // keep rotation when standing still
            attackVector.set(movementVector.x, movementVector.y);
        } else {
            attackVector = new Vector2f(goalPos.getX() - currentPos.getX(), goalPos.getY() - currentPos.getY());
        }
//        attackVector.set(attackVector.x, attackVector.y);
        movementVector.scale(SPEED_MULTIPLIER);

        super.onNewFrame();
    }
}
