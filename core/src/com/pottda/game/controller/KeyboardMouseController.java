package com.pottda.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ViewActor;

/**
 * Created by Rikard on 2017-04-05.
 */

public class KeyboardMouseController extends AbstractController {
    private final Stage stage;

    private static final float SPEED_MULT = 3;

    public KeyboardMouseController(ModelActor modelActor, ViewActor viewActor, Stage stage) {
        super(modelActor, viewActor);
        this.stage = stage;
    }


    @Override
    public void onNewFrame() {
        movementVector.set((Gdx.input.isKeyPressed(Input.Keys.D) ? 1 : 0 - (Gdx.input.isKeyPressed(Input.Keys.A) ? 1 : 0)) * SPEED_MULT,
                (Gdx.input.isKeyPressed(Input.Keys.W) ? 1 : 0 - (Gdx.input.isKeyPressed(Input.Keys.S) ? 1 : 0)) * SPEED_MULT);

        final Vector3 vector3 = stage.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        final float mousePosX = vector3.x;
        final float mousePosY = vector3.y;

        final float xDiff = mousePosX - modelActor.getPosition().x;
        final float yDiff = mousePosY - modelActor.getPosition().y;

        // y-axis is inverted in Box2D
        attackVector.set(xDiff, -yDiff);

        super.onNewFrame();
    }
}
