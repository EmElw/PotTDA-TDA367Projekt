package com.pottda.game.model;

import javax.vecmath.Vector2f;
import java.util.Collection;
import java.util.List;

/**
 * {@inheritDoc}
 * <p>
 * Specifically, a {@link Projectile}
 */
public interface BuilderProjectile extends BuilderModel {

    BuilderProjectile setBouncy();

    BuilderProjectile setBouncy(boolean bouncy);

    BuilderProjectile setPiercing();

    BuilderProjectile setPiercing(boolean piercing);

    BuilderProjectile setListeners(List<ProjectileListener> collection);

    BuilderProjectile setDamage(int n);

    BuilderProjectile setLifetime(long ms);

    BuilderProjectile setVelocity(Vector2f velocity);

}
