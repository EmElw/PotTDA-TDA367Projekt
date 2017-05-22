package com.pottda.game.model;

public interface PhysicsActorFactory {
    PhysicsActor getProjectilePhysicsActor(Projectile projectile);

    PhysicsActor getCharacterPhysicsActor(Character character);

    PhysicsActor getObstaclePhysicsActor(Obstacle obstacle);
}
