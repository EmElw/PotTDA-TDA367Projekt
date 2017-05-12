package com.pottda.game.controller;

import com.pottda.game.controller.AIController;
import com.pottda.game.model.ModelActor;
import com.pottda.game.pathfindingGDXAI.GDXAIPathfinder;
import com.pottda.game.view.ViewActor;

public class PathfindingAIController extends AIController {
    private GDXAIPathfinder pathfinder;
    public PathfindingAIController(ModelActor modelActor, ViewActor viewActor, GDXAIPathfinder pathfinder) {
        super(modelActor, viewActor);
        this.pathfinder = pathfinder;
    }

    public void onNewFrame(){
        movementVector = pathfinder.getPath(modelActor.getPosition());
        attackVector = movementVector;
        super.onNewFrame();
    }
}
