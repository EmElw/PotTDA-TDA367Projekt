package com.pottda.game.model.builders;

import com.pottda.game.model.Obstacle;

import javax.vecmath.Tuple2f;


/**
 * {@inheritDoc}
 * <p>
 * Specifically, an {@link Obstacle}
 */
public interface IObstacleBuilder extends IModelBuilder {


    IObstacleBuilder setSize(float width, float heigth);

    IObstacleBuilder setSize(Tuple2f dimensions);

}
