package com.pottda.game.model;

import javax.vecmath.Tuple2f;

public interface PhysicsActorFactory {
    PhysicsActor getProjectilePhysicsActor(Projectile projectile);

    PhysicsActor getCharacterPhysicsActor(Character character);

    PhysicsActor getObstaclePhysicsActor(Obstacle obstacle);
}
