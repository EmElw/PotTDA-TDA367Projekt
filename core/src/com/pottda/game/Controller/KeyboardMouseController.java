package com.pottda.game.Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2f;

/**
 * Created by Rikard on 2017-04-05.
 */

public class KeyboardMouseController extends InputController {
    private final Body body;

    public KeyboardMouseController(List<AttackListener> attackListeners, List<MovementListener> movementListeners, boolean isAI, Body body) {
        this.attackListeners = attackListeners;
        this.movementListeners = movementListeners;
        this.isAI = isAI;
        this.body = body;

        movementVector = new Vector2f(0, 0);
        attackVector = new Vector2f(0, 0);
    }

    @Override
    public void control() {
        movementVector.set(Gdx.input.isKeyPressed(Input.Keys.D) ? 1 : 0 - (Gdx.input.isKeyPressed(Input.Keys.A) ? 1 : 0),
                Gdx.input.isKeyPressed(Input.Keys.W) ? 1 : 0 - (Gdx.input.isKeyPressed(Input.Keys.S) ? 1 : 0));

        float mousePosX = Gdx.input.getX();
        float mousePosY = Gdx.input.getY();

        final float xDiff = mousePosX - body.getPosition().x;
        final float yDiff = mousePosY - body.getPosition().y;

        // y-axis is inverted in Box2D
        attackVector.set(xDiff, -yDiff);
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
