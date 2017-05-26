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
    // TODO access in nicer way
    private Skin touchpadSkin = new Skin(Gdx.files.internal("skin/quantum-horizon-ui.json"));

    public JoysticksView(Stage stage) {
        this.stage = stage;
        createMovementJoystick();
        createAttackJoystick();
    }

    private void createAttackJoystick() {

        attackTouchpad = new Touchpad(10, touchpadSkin);
        attackTouchpad.setBounds(stage.getWidth() - 105, 15, 90, 90);

        stage.addActor(attackTouchpad);
    }

    private void createMovementJoystick() {

        movementTouchpad = new Touchpad(10, touchpadSkin);
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
