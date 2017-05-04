package com.pottda.game.Controller;

import com.pottda.game.Model.ModelActor;
import com.pottda.game.View.ViewActor;

import java.util.List;

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

    private void updateView() {
        // TODO extend with other modifications such as rotation, scale etc.
        Vector2f position = modelActor.getPosition();
        viewActor.setPosition(position.x, position.y);
    }


}
