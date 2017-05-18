package com.pottda.game.model;

import javax.vecmath.Tuple2f;
import javax.vecmath.Vector2f;

/**
 * Concrete implementation for an Obstacle Builder pattern
 */
public class ObstacleBuilder extends AbstractModelBuilder implements BuilderObstacle {

    private Tuple2f size = new Vector2f(0, 0);

    @Override
    public ModelActor create() {
        Obstacle obstacle = new Obstacle(
                null // TODO
        );
        setCommonParameters(obstacle);

        notifyListeners(obstacle);
        return obstacle;
    }

    // ---------- Setters

    @Override
    public BuilderObstacle setSize(float width, float heigth) {
        size.set(width, heigth);
        return this;
    }

    @Override
    public BuilderObstacle setSize(Tuple2f dimensions) {
        size.set(dimensions);
        return this;
    }
}
