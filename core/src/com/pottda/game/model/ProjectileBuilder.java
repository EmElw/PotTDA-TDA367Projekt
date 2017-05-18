package com.pottda.game.model;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magnus on 2017-05-18.
 */
public class ProjectileBuilder extends AbstractModelBuilder implements BuilderProjectile {


    private Boolean bouncy = false;
    private Boolean piercing = false;
    private List<ProjectileListener> listeners = new ArrayList<ProjectileListener>();
    private int damage = 0;
    private long lifetime = Projectile.DEFAULT_PROJECTILE_LIFETIME_MS;
    private Vector2f velocity = new Vector2f(0, 0);


    @Override
    public ModelActor create() {

        Projectile projectile = new Projectile(null,    // TODO
                damage,
                listeners,
                lifetime);

        projectile.isBouncy = bouncy;
        projectile.isPiercing = piercing;
        projectile.giveInput(velocity, null);

        setCommonParameters(projectile);

        return projectile;

    }

    // ------------ Setters ---------

    @Override
    public BuilderProjectile setBouncy() {
        return setBouncy(true);
    }

    @Override
    public BuilderProjectile setBouncy(boolean bouncy) {
        this.bouncy = bouncy;
        return this;
    }

    @Override
    public BuilderProjectile setPiercing() {
        return setPiercing(true);
    }

    @Override
    public BuilderProjectile setPiercing(boolean piercing) {
        this.piercing = piercing;
        return this;
    }

    @Override
    public BuilderProjectile setListeners(List<ProjectileListener> list) {
        this.listeners.addAll(list);
        return this;
    }

    @Override
    public BuilderProjectile setDamage(int n) {
        this.damage = n;
        return this;
    }

    @Override
    public BuilderProjectile setLifetime(long ms) {
        this.lifetime = ms;
        return this;
    }

    @Override
    public BuilderProjectile setVelocity(Vector2f velocity) {
        this.velocity = velocity;
        return this;
    }

}
