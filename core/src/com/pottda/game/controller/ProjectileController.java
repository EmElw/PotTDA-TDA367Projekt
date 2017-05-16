package com.pottda.game.controller;

import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ViewActor;

public class ProjectileController extends AbstractController {
    public ProjectileController(ModelActor modelActor, ViewActor viewActor) {
        super(modelActor, viewActor);
        // movementVector.set(0, 0);
    }

    @Override
    protected void setInputVectors() {

    }
}
