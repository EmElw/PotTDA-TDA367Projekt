package com.pottda.game.Controller;

import java.util.List;

import javax.vecmath.Vector2f;

/**
 * Created by Rikard Teodorsson on 2017-04-07.
 */

public abstract class AbstractController {
    Vector2f movementVector;
    Vector2f attackVector;
    List<AttackListener> attackListeners;
    List<MovementListener> movementListeners;
    boolean isAI;

    public abstract void control();

    public abstract List<AttackListener> getAttackListeners();

    public abstract List<MovementListener> getMovementListeners();

    public abstract void addAttackListener(AttackListener attackListener);

    public abstract void addMovementListener(MovementListener movementListener);

    public abstract Vector2f getMovementVector();

    public abstract Vector2f getAttackVector();

}
