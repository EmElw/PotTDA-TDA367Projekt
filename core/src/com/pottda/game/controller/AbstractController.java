package com.pottda.game.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
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
     *
     * @param modelActor
     * @param viewActor
     * @param stage the stage to add the actor to (hudStage or gameStage)
     */
    AbstractController(ModelActor modelActor, ViewActor viewActor, Stage stage) {
        this.modelActor = modelActor;
        this.viewActor = viewActor;
        movementVector = new Vector2f(0,0);
        attackVector = new Vector2f(0,0);

        stage.addActor(viewActor);
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
