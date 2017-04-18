package com.pottda.game.Model;


import com.pottda.game.Controller.AbstractController;

/**
 * Created by Gustav Lahti on 2017-04-07.
 */

public abstract class Actor {
    public boolean isProjectile;
    public int team;
    public AbstractController controller;

    public abstract void collide(Actor other);

    //public abstract VectorType getMove();
}
