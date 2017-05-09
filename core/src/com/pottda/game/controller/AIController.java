package com.pottda.game.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ViewActor;

/**
 * Created by Rikard Teodorsson on 2017-04-07.
 */

public abstract class AIController extends AbstractController {
    public AIController(ModelActor modelActor, ViewActor viewActor, Stage stage) {
        super(modelActor, viewActor, stage);
    }


    // https://github.com/libgdx/gdx-ai/wiki

}
