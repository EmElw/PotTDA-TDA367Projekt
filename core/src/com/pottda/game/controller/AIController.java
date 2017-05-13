package com.pottda.game.controller;

import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ViewActor;

/**
 * Created by Rikard Teodorsson on 2017-04-07.
 */

public abstract class AIController extends AbstractController {
    public final static float SPEED_MULTIPLIER = 1.5f;
    public AIController(ModelActor modelActor, ViewActor viewActor) {
        super(modelActor, viewActor);
    }


    // https://github.com/libgdx/gdx-ai/wiki

}
