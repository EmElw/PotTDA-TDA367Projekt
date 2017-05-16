package com.pottda.game.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class ControllerOptions {

    public enum ControllerMode{
        TOUCH_JOYSTICK,
        KEYBOARD_MOUSE,
        KEYBOARD_ONLY
    }

    public static Stage joystickStage;
    public static ControllerMode controllerSettings = ControllerMode.KEYBOARD_ONLY;
}
