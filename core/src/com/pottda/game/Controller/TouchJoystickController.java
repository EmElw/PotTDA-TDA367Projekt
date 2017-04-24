package com.pottda.game.Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2f;

/**
 * Created by Rikard Teodorsson on 2017-04-07.
 */

public class TouchJoystickController extends InputController {
    private final Stage stage;
    private Touchpad movementTouchpad;
    private Touchpad attackTouchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;

    public TouchJoystickController(ArrayList<AttackListener> attackListeners, ArrayList<MovementListener> movementListeners, boolean isAI, Stage stage) {
        this.attackListeners = attackListeners;
        this.movementListeners = movementListeners;
        this.isAI = isAI;
        this.stage = stage;

        movementVector = new Vector2f(0, 0);
        attackVector = new Vector2f(0, 0);

        createMovementJoystick();
        createAttackJoystick();
    }

    private void createAttackJoystick() {
        //Create a movementTouchpad skin
        touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("touchBackground.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("touchKnob.png"));
        //Create TouchPad Style
        touchpadStyle = new Touchpad.TouchpadStyle();
        //Create Drawable's from TouchPad skin
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        touchKnob.setMinHeight(30);
        touchKnob.setMinWidth(30);
        //Create new TouchPad with the created style
        attackTouchpad = new Touchpad(10, touchpadStyle);
        //setBounds(x, y, width, height)
        attackTouchpad.setBounds(Gdx.graphics.getWidth() - 105, 15, 90, 90);

        //add TouchPad to stage
        stage.addActor(attackTouchpad);
    }

    private void createMovementJoystick(){
        //Create a movementTouchpad skin
        touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("touchBackground.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("touchKnob.png"));
        //Create TouchPad Style
        touchpadStyle = new Touchpad.TouchpadStyle();
        //Create Drawable's from TouchPad skin
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        touchKnob.setMinHeight(30);
        touchKnob.setMinWidth(30);
        //Create new TouchPad with the created style
        movementTouchpad = new Touchpad(10, touchpadStyle);
        //setBounds(x,y,width,height)
        movementTouchpad.setBounds(15, 15, 90, 90);

        //add TouchPad to stage
        stage.addActor(movementTouchpad);
        Gdx.input.setInputProcessor(stage);
    }

    public void control() {
        movementVector.set(movementTouchpad.getKnobPercentX(), movementTouchpad.getKnobPercentY());
        attackVector.set(attackTouchpad.getKnobPercentX(), attackTouchpad.getKnobPercentY());
    }

    @Override
    public List<AttackListener> getAttackListeners() {
        return attackListeners;
    }

    @Override
    public List<MovementListener> getMovementListeners() {
        return movementListeners;
    }

    @Override
    public void addAttackListener(AttackListener attackListener) {
        attackListeners.add(attackListener);
    }

    @Override
    public void addMovementListener(MovementListener movementListener) {
        movementListeners.add(movementListener);
    }

    @Override
    public Vector2f getMovementVector() {
        return this.movementVector;
    }

    @Override
    public Vector2f getAttackVector() {
        return this.attackVector;
    }
}
