package com.pottda.game.Controller;

import com.pottda.game.Model.ModelActor;
import com.pottda.game.View.ViewActor;

/**
 * Created by Rikard Teodorsson on 2017-04-07.
 */

public abstract class AIController extends AbstractController {
    public AIController(ModelActor modelActor, ViewActor viewActor) {
        super(modelActor, viewActor);
    }


    // https://github.com/libgdx/gdx-ai/wiki

}
