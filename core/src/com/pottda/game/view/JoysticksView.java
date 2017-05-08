package com.pottda.game.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Created by Rikard Teodorsson on 2017-05-08.
 */

public class JoysticksView extends ApplicationAdapter {
    private final Stage stage;
    private Touchpad movementTouchpad;
    private Touchpad attackTouchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;

    public JoysticksView(Stage stage) {
        createMovementJoystick();
        createAttackJoystick();
        this.stage = stage;
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

    private void createMovementJoystick() {
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

    public float getMovementKnobX() {
        return movementTouchpad.getKnobPercentX();
    }

    public float getMovementKnobY() {
        return movementTouchpad.getKnobPercentY();
    }

    public float getAttackKnobX() {
        return attackTouchpad.getKnobPercentX();
    }

    public float getAttackKnobY() {
        return attackTouchpad.getKnobPercentY();
    }

}
