package com.pottda.game.model.builders;

import com.pottda.game.model.ModelActor;
import com.pottda.game.model.Projectile;
import com.pottda.game.model.ProjectileListener;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magnus on 2017-05-18.
 */
public class ProjectileBuilder extends AbstractModelBuilder implements IProjectileBuilder {


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

        setCommonAndNotify(projectile);
        return projectile;

    }

    // ------------ Setters ---------

    @Override
    public IProjectileBuilder setBouncy() {
        return setBouncy(true);
    }

    @Override
    public IProjectileBuilder setBouncy(boolean bouncy) {
        this.bouncy = bouncy;
        return this;
    }

    @Override
    public IProjectileBuilder setPiercing() {
        return setPiercing(true);
    }

    @Override
    public IProjectileBuilder setPiercing(boolean piercing) {
        this.piercing = piercing;
        return this;
    }

    @Override
    public IProjectileBuilder setListeners(List<ProjectileListener> list) {
        this.listeners.addAll(list);
        return this;
    }

    @Override
    public IProjectileBuilder setDamage(int n) {
        this.damage = n;
        return this;
    }

    @Override
    public IProjectileBuilder setLifetime(long ms) {
        this.lifetime = ms;
        return this;
    }

    @Override
    public IProjectileBuilder setVelocity(Vector2f velocity) {
        this.velocity = velocity;
        return this;
    }

}
