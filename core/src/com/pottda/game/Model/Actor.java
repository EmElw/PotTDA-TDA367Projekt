package com.pottda.game.Model;

import java.util.Vector;

/**
 * Created by rikar on 2017-04-07.
 */

public abstract class Actor {
    public boolean isProjectile;
    public int team;

    public void collide(Actor actor) {

    }

    public abstract Vector getMove();
}
