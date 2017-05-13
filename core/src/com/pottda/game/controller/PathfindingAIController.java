package com.pottda.game.controller;

import com.pottda.game.controller.AIController;
import com.pottda.game.model.ModelActor;
import com.pottda.game.pathfindingGDXAI.GDXAIPathfinder;
import com.pottda.game.view.ViewActor;

import javax.vecmath.Vector2f;

public class PathfindingAIController extends AIController {
    private final static int UPDATE_COOLDOWN_TIME = 1000;

    private GDXAIPathfinder pathfinder;
    private long lastUpdateTime;

    public PathfindingAIController(ModelActor modelActor, ViewActor viewActor, GDXAIPathfinder pathfinder) {
        super(modelActor, viewActor);
        this.pathfinder = pathfinder;
    }

    public void onNewFrame(){
//        if (System.currentTimeMillis() - lastUpdateTime > UPDATE_COOLDOWN_TIME){
//            lastUpdateTime = System.currentTimeMillis();
            updateVectors();
//        }

        super.onNewFrame();
    }

    private void updateVectors(){
        movementVector = pathfinder.getPath(modelActor.getPosition());
        attackVector.set(movementVector.x, movementVector.y);
        attackVector.normalize();

        if (movementVector.length() < 3) {
            movementVector.set(0, 0);
        } else {
            movementVector.normalize();
            movementVector.scale(SPEED_MULTIPLIER);
        }

//        movementVector.normalize();
    }
}
