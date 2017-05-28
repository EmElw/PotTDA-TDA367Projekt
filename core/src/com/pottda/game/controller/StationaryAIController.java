package com.pottda.game.controller;

import com.pottda.game.model.Character;
import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ActorView;

class StationaryAIController extends AIController{
    StationaryAIController(ModelActor modelActor, ActorView actorView, EnemyHealthBarController enemyHealthBarController) {
        super(modelActor, actorView, enemyHealthBarController);
    }

    @Override
    protected void setInputVectors() {
//        movementVector.set(0, 0);
        if(Character.getPlayer() != null && Character.getPlayer().getPosition() != null) {
            attackVector.set(Character.getPlayer().getPosition());
            attackVector.sub(modelActor.getPosition());
            attackVector.normalize();
        }
    }
}
