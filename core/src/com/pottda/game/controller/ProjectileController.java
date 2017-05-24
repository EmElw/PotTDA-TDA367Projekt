package com.pottda.game.controller;

import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ActorView;

class ProjectileController extends AbstractController {
    ProjectileController(ModelActor modelActor, ActorView actorView) {
        super(modelActor, actorView);
    }

    @Override
    protected void setInputVectors() {
    }
}
