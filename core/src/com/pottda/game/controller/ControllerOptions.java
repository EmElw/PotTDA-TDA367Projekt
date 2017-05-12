package com.pottda.game.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class ControllerOptions {
    public final static int TOUCH_JOYSTICK = 0;
    public final static int KEYBOARD_MOUSE = 1;
    public final static int KEYBOARD_ONLY = 2;

    public static Stage joystickStage;
    public static int controllerSettings = KEYBOARD_ONLY;
}
