package com.pottda.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ViewActor;

/**
 * Created by Rikard Teodorsson on 2017-04-07.
 */

public class KeyboardOnlyController extends AbstractController {

    public KeyboardOnlyController(ModelActor modelActor, ViewActor viewActor) {
        super(modelActor, viewActor);
    }

    @Override
    public void onNewFrame() {
        movementVector.set(Gdx.input.isKeyPressed(Input.Keys.D) ? 1 : 0 - (Gdx.input.isKeyPressed(Input.Keys.A) ? 1 : 0),
                Gdx.input.isKeyPressed(Input.Keys.W) ? 1 : 0 - (Gdx.input.isKeyPressed(Input.Keys.S) ? 1 : 0));

        attackVector.set(Gdx.input.isKeyPressed(Input.Keys.RIGHT) ? 1 : 0 - (Gdx.input.isKeyPressed(Input.Keys.LEFT) ? 1 : 0),
                Gdx.input.isKeyPressed(Input.Keys.UP) ? 1 : 0 - (Gdx.input.isKeyPressed(Input.Keys.DOWN) ? 1 : 0));
        super.onNewFrame();
    }
}
