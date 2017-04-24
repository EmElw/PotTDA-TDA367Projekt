package com.pottda.game.Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.List;

import javax.vecmath.Vector2f;

/**
 * Created by Rikard Teodorsson on 2017-04-07.
 */

public class KeyboardOnlyController extends InputController {

    public KeyboardOnlyController(List<AttackListener> attackListeners, List<MovementListener> movementListeners, boolean isAI) {
        this.attackListeners = attackListeners;
        this.movementListeners = movementListeners;
        this.isAI = isAI;

        movementVector = new Vector2f(0, 0);
        attackVector = new Vector2f(0, 0);
    }

    @Override
    public void control() {
        movementVector.set(Gdx.input.isKeyPressed(Input.Keys.D) ? 1 : 0 - (Gdx.input.isKeyPressed(Input.Keys.A) ? 1 : 0),
                Gdx.input.isKeyPressed(Input.Keys.W) ? 1 : 0 - (Gdx.input.isKeyPressed(Input.Keys.S) ? 1 : 0));

        attackVector.set(Gdx.input.isKeyPressed(Input.Keys.RIGHT) ? 1 : 0 - (Gdx.input.isKeyPressed(Input.Keys.LEFT) ? 1 : 0),
                Gdx.input.isKeyPressed(Input.Keys.UP) ? 1 : 0 - (Gdx.input.isKeyPressed(Input.Keys.DOWN) ? 1 : 0));
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
