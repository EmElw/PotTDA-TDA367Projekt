package com.pottda.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Magnus on 2017-04-05.
 */
public class TouchJoystickController extends AbstractController implements InputProcessor {

    private int joystickRadius = 40;

    private Vector2 leftStickOrigin;
    private Vector2 leftInput;
    private int leftStickPointer;
    private boolean leftStick;

    private Vector2 rightStickOrigin;
    private Vector2 rightInput;
    private int rightStickPointer;
    private boolean rightStick;

    TouchJoystickController() {
        leftStickOrigin = new Vector2();
        leftInput = new Vector2();

        rightStickOrigin = new Vector2();
        rightInput = new Vector2();

    }

    private ControlMethod controlMethod = ControlMethod.LEFT_IS_MOVE;

    public enum ControlMethod {
        LEFT_IS_MOVE,
        RIGHT_IS_MOVE
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (screenX < Gdx.graphics.getWidth() / 2) {    // Check on left side of screen
            if (!leftStick) {   // Check if leftStick is available
                leftStick = true;   // Mark the leftStick as taken
                leftStickPointer = pointer;     // Associate the pointer (i.e. touchIndex) with the stick
                leftStickOrigin.set(screenX, screenY);  // Set the sticks origin
            }
        } else {
            if (!rightStick) {
                rightStick = true;
                rightStickPointer = pointer;
                leftStickOrigin.set(screenX, screenY);
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer == leftStickPointer) {  // If the pointer associated with the left stick is released
            leftStick = false;  // mark the leftStick as available
            leftInput.set(0, 0);
        }
        if (pointer == rightStickPointer) {
            rightStick = false;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pointer == leftStickPointer) {
            leftInput.set(leftStickOrigin.sub(screenX, screenY)).clamp(0, joystickRadius);
        }
        if (pointer == rightStickPointer) {
            rightInput.set(rightStickOrigin.sub(screenX, screenY)).clamp(0, joystickRadius);
        }

        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void control(Controllable c) {
        switch (controlMethod) {
            case LEFT_IS_MOVE:
                c.move(leftInput);
                c.attack(rightInput.angleRad());
                break;
            case RIGHT_IS_MOVE:
                c.move(rightInput);
                c.attack(leftInput.angleRad());
                break;
        }
    }
}
