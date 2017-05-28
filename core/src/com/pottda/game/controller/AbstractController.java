package com.pottda.game.controller;

import com.pottda.game.model.ModelActor;
import com.pottda.game.model.Obstacle;
import com.pottda.game.view.ActorView;

import javax.vecmath.Vector2f;

/**
 * Usage:
 * Classes implementing should in onNewFrame() update
 * their move- and attackVector before calling super.onNewFrame()
 */

public abstract class AbstractController {
    final Vector2f movementVector;
    final Vector2f attackVector;

    final ModelActor modelActor;
    private final ActorView actorView;

    /**
     * Creates a new AbstractController and sets its
     * actor and view
     *
     * @param modelActor a {@link ModelActor}
     * @param actorView  a {@link ActorView}
     */
    AbstractController(ModelActor modelActor, ActorView actorView) {
        this.modelActor = modelActor;
        this.actorView = actorView;
        movementVector = new Vector2f(0, 0);
        attackVector = new Vector2f(0, 0);
    }

    /**
     * @param delta
     */
    public void update(float delta) {
        setInputVectors();
        updateModel(delta);
        updateView();
    }

    /**
     * Called every frame and should set the movement- and attack
     * vectors of the controller.
     * <p>
     * {@code movementVector} should be of 0 <= length <= 1
     * <p>
     * {@code attackVector} should be normalized or 0,0
     */
    protected abstract void setInputVectors();

    public boolean shouldBeRemoved() {
        return modelActor.shouldBeRemoved;
    }

    public void updateModel(float delta) {
        modelActor.giveInput(movementVector, attackVector);
        modelActor.update(delta);
    }

    private void updateView() {
        // TODO extend with other modifications such as rotation and stuff
        Vector2f position = modelActor.getPosition();

        if (modelActor instanceof Obstacle) {
            actorView.setPoint(position.x, position.y, true);
        } else {
            actorView.setPoint(position.x, position.y, false);
        }
        actorView.setAngle(modelActor.getAngle());
    }

    public ModelActor getModel() {
        return modelActor;
    }

    public ActorView getView() {
        return actorView;
    }
}
