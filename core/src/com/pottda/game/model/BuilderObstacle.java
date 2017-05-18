package com.pottda.game.model;

import javax.vecmath.Tuple2f;


/**
 * {@inheritDoc}
 * <p>
 * Specifically, an {@link Obstacle}
 */
public interface BuilderObstacle extends BuilderModel {


    BuilderObstacle setSize(float width, float heigth);

    BuilderObstacle setSize(Tuple2f dimensions);

}
