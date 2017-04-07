package com.pottda.game.Model;

/**
 * Created by rikar on 2017-04-07.
 */

public class Player extends Character {

    public Player(Inventory inventory, int team) {
        super(inventory, team);
    }

    @Override
    public void collide(Actor other) {

    }
}
