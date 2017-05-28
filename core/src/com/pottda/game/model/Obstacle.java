package com.pottda.game.model;

import javax.vecmath.Tuple2f;

public class Obstacle extends ModelActor {
    public Tuple2f size;
    public Boolean isRound;


    public Obstacle() {
    }

    @Override
    public void update(float delta) {

    }

    public Tuple2f getSize() {
        return size;
    }

    public void setSize(Tuple2f size) {
        this.size = size;
    }

    public Boolean getRound() {
        return isRound;
    }

    public void setRound(Boolean round) {
        isRound = round;
    }
}
