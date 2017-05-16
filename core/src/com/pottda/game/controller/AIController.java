package com.pottda.game.controller;

import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ActorView;

/**
 * Created by Rikard Teodorsson on 2017-04-07.
 */

public abstract class AIController extends AbstractController {
    AIController(ModelActor modelActor, ActorView actorView) {
        super(modelActor, actorView);
    }


    // https://github.com/libgdx/gdx-ai/wiki

}
