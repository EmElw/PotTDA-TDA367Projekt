package com.pottda.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ActorView;

/**
 * Created by Rikard on 2017-04-05.
 */

public class KeyboardMouseController extends AbstractController {
    private final Stage stage;

    public KeyboardMouseController(ModelActor modelActor, ActorView actorView, Stage stage) {
        super(modelActor, actorView);
        this.stage = stage;
    }


    @Override
    public void setInputVectors() {
        movementVector.set((Gdx.input.isKeyPressed(Input.Keys.D) ? 1 : 0 - (Gdx.input.isKeyPressed(Input.Keys.A) ? 1 : 0)) * SPEED_MULT,
                (Gdx.input.isKeyPressed(Input.Keys.W) ? 1 : 0 - (Gdx.input.isKeyPressed(Input.Keys.S) ? 1 : 0)) * SPEED_MULT);
        movementVector.normalize();

        final Vector3 vector3 = stage.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        final float mousePosX = vector3.x;
        final float mousePosY = vector3.y;

        final float xDiff = mousePosX - modelActor.getPosition().x;
        final float yDiff = mousePosY - modelActor.getPosition().y;

        attackVector.set(xDiff, yDiff);
        attackVector.normalize();
    }
}
