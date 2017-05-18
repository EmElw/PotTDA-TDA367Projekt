package com.pottda.game.model.builders;

import com.pottda.game.model.Projectile;
import com.pottda.game.model.ProjectileListener;

import javax.vecmath.Vector2f;
import java.util.List;

/**
 * {@inheritDoc}
 * <p>
 * Specifically, a {@link Projectile}
 */
public interface IProjectileBuilder extends IModelBuilder {

    IProjectileBuilder copyProperties(Projectile p);

    IProjectileBuilder setTeam(int n);

    IProjectileBuilder setBouncy();

    IProjectileBuilder setBouncy(boolean bouncy);

    IProjectileBuilder setPiercing();

    IProjectileBuilder setPiercing(boolean piercing);

    IProjectileBuilder setListeners(List<ProjectileListener> collection);

    IProjectileBuilder setDamage(int n);

    IProjectileBuilder setLifetime(long ms);

    IProjectileBuilder setVelocity(Vector2f velocity);

}
