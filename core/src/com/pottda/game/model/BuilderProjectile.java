package com.pottda.game.model;

import javax.vecmath.Vector2f;
import java.util.Collection;

/**
 * {@inheritDoc}
 * <p>
 * Specifically, a {@link Projectile}
 */
public interface BuilderProjectile extends BuilderModel {

    BuilderProjectile setBouncy();

    BuilderProjectile setBouncy(boolean bouncy);

    BuilderProjectile setPenetrating();

    BuilderProjectile setPenetrating(boolean piercing);

    BuilderProjectile setListeners(Collection<ProjectileListener> collection);

    BuilderProjectile setDamage(int n);

    BuilderProjectile setLifetime(long ms);

    BuilderProjectile setVelocity(Vector2f velocity);

    BuilderProjectile setDirection(Vector2f direction);

    BuilderProjectile setDirection(float rad);

    BuilderProjectile setSpeed(float speed);
}
