package com.pottda.game.controller;

import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ViewActor;

public class ObstacleController extends AbstractController {
    public ObstacleController(ModelActor modelActor, ViewActor viewActor) {
        super(modelActor, viewActor);
    }

    @Override
    public void onNewFrame() {
        updateView();
    }
}
