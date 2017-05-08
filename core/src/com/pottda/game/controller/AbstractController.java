package com.pottda.game.controller;

import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ViewActor;

import javax.vecmath.Vector2f;

/**
 * Usage:
 * Classes implementing should in onNewFrame() update
 * their move- and attackVector before calling super.onNewFrame()
 */

public abstract class AbstractController {
    Vector2f movementVector;
    Vector2f attackVector;
//    final boolean isAI;

    final ModelActor modelActor;
    final ViewActor viewActor;

    public AbstractController(ModelActor modelActor, ViewActor viewActor) {
        this.modelActor = modelActor;
        this.viewActor = viewActor;
    }

    /**
     * Called by MyGame every frame
     */
    public void onNewFrame() {
        updateModel();
        updateView();
    }

    private void updateModel() {
        modelActor.giveInput(movementVector, attackVector);
        modelActor.handleCollisions();
    }

    /**
     * Updates the ViewActor so everything can be drawn out later
     */
    private void updateView() {
        // TODO extend with other modifications such as rotation and stuff
        Vector2f position = modelActor.getPosition();
        float degrees = modelActor.getAngle();

        viewActor.setPoint(position.x, position.y);
        viewActor.setAngle(degrees);
    }


}
