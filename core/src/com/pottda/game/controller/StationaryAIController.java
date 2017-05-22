package com.pottda.game.controller;

import com.pottda.game.model.Character;
import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ActorView;

public class StationaryAIController extends AIController{
    public StationaryAIController(ModelActor modelActor, ActorView actorView) {
        super(modelActor, actorView);
    }

    @Override
    protected void setInputVectors() {
        movementVector.set(0, 0);
        attackVector.set(Character.player.getPosition());
        attackVector.sub(modelActor.getPosition());
    }
}
