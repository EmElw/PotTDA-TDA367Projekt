package com.pottda.game.controller;

import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ActorView;

import javax.vecmath.Vector2f;

/**
 * Usage:
 * Classes implementing should in onNewFrame() update
 * their move- and attackVector before calling super.onNewFrame()
 */

public abstract class AbstractController {
    Vector2f movementVector;
    Vector2f attackVector;
    static final float SPEED_MULT = 3;
//    final boolean isAI;

    final ModelActor modelActor;
    private final ActorView actorView;

    /**
     * @param modelActor
     * @param actorView
     */
    AbstractController(ModelActor modelActor, ActorView actorView) {
        this.modelActor = modelActor;
        this.actorView = actorView;
        movementVector = new Vector2f(0, 0);
        attackVector = new Vector2f(0, 0);
    }

    /**
     * Called by MyGame every frame
     */
    public void onNewFrame() {
        updateModel();
        updateView();
    }

    public boolean shouldBeRemoved() {
        return modelActor.shouldBeRemoved;
    }

    protected abstract void setInputVectors();

    private void updateModel() {
        modelActor.giveInput(movementVector, attackVector);
        modelActor.handleCollisions();
    }

    /**
     * Updates the ActorView so everything can be drawn out later
     */
    protected void updateView() {
        // TODO extend with other modifications such as rotation and stuff
        Vector2f position = modelActor.getPosition();
//        float degrees = modelActor.getAngle();

        actorView.setPoint(position.x, position.y);
        actorView.setAngle(modelActor.getAngle());
    }

    public ModelActor getModel() {
        return modelActor;
    }

    public ActorView getView() {
        return actorView;
    }
}
