package com.pottda.game.Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import com.pottda.game.Model.ModelActor;
import com.pottda.game.View.ViewActor;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2f;

/**
 * Created by Rikard on 2017-04-05.
 */

public class KeyboardMouseController extends AbstractController {

    public KeyboardMouseController(ModelActor modelActor, ViewActor viewActor) {
        super(modelActor, viewActor);
    }


    @Override
    public void onNewFrame() {
        movementVector.set(Gdx.input.isKeyPressed(Input.Keys.D) ? 1 : 0 - (Gdx.input.isKeyPressed(Input.Keys.A) ? 1 : 0),
                Gdx.input.isKeyPressed(Input.Keys.W) ? 1 : 0 - (Gdx.input.isKeyPressed(Input.Keys.S) ? 1 : 0));

        float mousePosX = Gdx.input.getX();
        float mousePosY = Gdx.input.getY();

        final float xDiff = mousePosX - modelActor.getPosition().x;
        final float yDiff = mousePosY - modelActor.getPosition().y;

        // y-axis is inverted in Box2D
        attackVector.set(xDiff, -yDiff);

        super.onNewFrame();
    }
}
