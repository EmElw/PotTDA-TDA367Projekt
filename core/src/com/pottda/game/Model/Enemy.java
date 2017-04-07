package com.pottda.game.Model;

/**
 * Created by rikar on 2017-04-07.
 */

public class Enemy extends Character {

    public Enemy(Inventory inventory, int team) {
        super(inventory, team);
    }

    @Override
    public void collide(Actor other) {

    }
}
