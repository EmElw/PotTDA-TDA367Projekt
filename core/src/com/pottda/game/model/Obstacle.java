package com.pottda.game.model;

import javax.vecmath.Tuple2f;

public class Obstacle extends ModelActor {
    private Tuple2f size;
    private Boolean isRound;

    public Obstacle() {
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
