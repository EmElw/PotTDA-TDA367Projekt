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
    private final ViewActor viewActor;

    /**
     * @param modelActor
     * @param viewActor
     */
    protected AbstractController(ModelActor modelActor, ViewActor viewActor) {
        this.modelActor = modelActor;
        this.viewActor = viewActor;
        movementVector = new Vector2f();
        attackVector = new Vector2f();
    }

    /**
     * Called by MyGame every frame
     */
    public void onNewFrame() {
        setInputVectors();
        updateModel();
        updateView();
    }

    protected abstract void setInputVectors();

    public boolean shouldBeRemoved() {
        return modelActor.shouldBeRemoved;
    }

    private void updateModel() {
        modelActor.giveInput(movementVector, attackVector);
    }

    /**
     * Updates the ViewActor so everything can be drawn out later
     */
    protected void updateView() {
        // TODO extend with other modifications such as rotation and stuff
        Vector2f position = modelActor.getPosition();
//        float degrees = modelActor.getAngle();

        viewActor.setPoint(position.x, position.y);
        viewActor.setAngle(modelActor.getAngle());
    }

    public ModelActor getModel() {
        return modelActor;
    }

    public ViewActor getView() {
        return viewActor;
    }
}
