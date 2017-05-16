package com.pottda.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ActorView;

/**
 * Created by Rikard Teodorsson on 2017-04-07.
 */

public class KeyboardOnlyController extends AbstractController {

    KeyboardOnlyController(ModelActor modelActor, ActorView actorView) {
        super(modelActor, actorView);
    }

    @Override
    public void setInputVectors() {
        movementVector.set(
                (Gdx.input.isKeyPressed(Input.Keys.D) ? 1 : 0 -
                        (Gdx.input.isKeyPressed(Input.Keys.A) ? 1 : 0)),

                (Gdx.input.isKeyPressed(Input.Keys.W) ? 1 : 0 -
                        (Gdx.input.isKeyPressed(Input.Keys.S) ? 1 : 0)));

        if (movementVector.length() > 1) {
            movementVector.normalize();
        }

        attackVector.set(
                Gdx.input.isKeyPressed(Input.Keys.RIGHT) ? 1 : 0 -
                        (Gdx.input.isKeyPressed(Input.Keys.LEFT) ? 1 : 0),

                Gdx.input.isKeyPressed(Input.Keys.UP) ? 1 : 0 -
                        (Gdx.input.isKeyPressed(Input.Keys.DOWN) ? 1 : 0));

        attackVector.normalize();
    }
}
