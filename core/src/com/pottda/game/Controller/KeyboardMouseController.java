package com.pottda.game.Controller;


import com.badlogic.gdx.math.Vector2;
import com.pottda.game.Controller.InputController;

/**
 * Created by Magnus on 2017-04-05.
 */
public class KeyboardMouseController extends InputController {

    private Vector2 input;

    /*@Override
    public void control(Controllable c) {

        // Poll keyboard inputs to alter the hinput and vinput
        input.add(Gdx.input.isKeyPressed(Input.Keys.RIGHT) ? 1 : 0 -
                        (Gdx.input.isKeyPressed(Input.Keys.LEFT) ? 1 : 0),
                Gdx.input.isKeyPressed(Input.Keys.UP) ? 1 : 0 -
                        (Gdx.input.isKeyPressed(Input.Keys.DOWN) ? 1 : 0));

        c.move(input);

        if (Gdx.input.isTouched()) {
            Vector2 deltaVector = c.getPosition().sub(new Vector2(Gdx.input.getX(), Gdx.input.getY()));  // TODO might be wrong y-axis
            c.attack(deltaVector.angleRad());
        }
    }*/
}
