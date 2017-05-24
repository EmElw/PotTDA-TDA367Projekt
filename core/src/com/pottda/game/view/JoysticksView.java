package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class JoysticksView {
    private final Stage stage;
    private Touchpad movementTouchpad;
    private Touchpad attackTouchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;

    public JoysticksView(Stage stage) {
        this.stage = stage;
        createMovementJoystick();
        createAttackJoystick();
    }

    private void createAttackJoystick() {
        touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture("touchBackground.png"));
        touchpadSkin.add("touchKnob", new Texture("touchKnob.png"));

        touchpadStyle = new Touchpad.TouchpadStyle();
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;

        touchKnob.setMinHeight(30);
        touchKnob.setMinWidth(30);

        attackTouchpad = new Touchpad(10, touchpadStyle);
        attackTouchpad.setBounds(stage.getWidth() - 105, 15, 90, 90);

        stage.addActor(attackTouchpad);
    }

    private void createMovementJoystick() {
        touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture("touchBackground.png"));
        touchpadSkin.add("touchKnob", new Texture("touchKnob.png"));

        touchpadStyle = new Touchpad.TouchpadStyle();
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;

        touchKnob.setMinHeight(30);
        touchKnob.setMinWidth(30);

        movementTouchpad = new Touchpad(10, touchpadStyle);
        movementTouchpad.setBounds(15, 15, 90, 90);

        stage.addActor(movementTouchpad);
        Gdx.input.setInputProcessor(stage);
    }

    public void onNewFrame() {
        if (movementTouchpad.isTouched()) {
            movementTouchpad.setColor(movementTouchpad.getColor().r, movementTouchpad.getColor().g, movementTouchpad.getColor().b, (float) 0.4);
        } else {
            movementTouchpad.setColor(movementTouchpad.getColor().r, movementTouchpad.getColor().g, movementTouchpad.getColor().b, 1);
        }
        if (attackTouchpad.isTouched()) {
            attackTouchpad.setColor(attackTouchpad.getColor().r, attackTouchpad.getColor().g, attackTouchpad.getColor().b, (float) 0.4);
        } else {
            attackTouchpad.setColor(attackTouchpad.getColor().r, attackTouchpad.getColor().g, attackTouchpad.getColor().b, 1);
        }
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
