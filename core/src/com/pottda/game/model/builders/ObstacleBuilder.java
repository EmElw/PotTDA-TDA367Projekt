package com.pottda.game.model.builders;

import com.pottda.game.model.ModelActor;
import com.pottda.game.model.Obstacle;

import javax.vecmath.Tuple2f;
import javax.vecmath.Vector2f;

/**
 * Concrete implementation for an Obstacle Builder pattern
 */
public class ObstacleBuilder extends AbstractModelBuilder implements IObstacleBuilder {

    private final Tuple2f size = new Vector2f(0, 0);

    @Override
    public ModelActor create() {
        Obstacle obstacle = new Obstacle();
        obstacle.size = this.size;
        obstacle.setPhysicsActor(physiscActorFactory.getObstaclePhysicsActor(obstacle));
        setCommonAndNotify(obstacle);
        return obstacle;
    }

    // ---------- Setters

    @Override
    public IObstacleBuilder setSize(float width, float heigth) {
        size.set(width, heigth);
        return this;
    }

    @Override
    public IObstacleBuilder setSize(Tuple2f dimensions) {
        size.set(dimensions);
        return this;
    }
}
