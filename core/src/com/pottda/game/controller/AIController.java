package com.pottda.game.controller;

import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ActorView;

abstract class AIController extends AbstractController {
    /**
     * {@inheritDoc}
     * @param modelActor
     * @param actorView
     */
    AIController(ModelActor modelActor, ActorView actorView) {
        super(modelActor, actorView);
    }
}
