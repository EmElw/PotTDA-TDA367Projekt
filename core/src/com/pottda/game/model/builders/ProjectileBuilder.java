package com.pottda.game.model.builders;

import com.pottda.game.model.ModelActor;
import com.pottda.game.model.Projectile;
import com.pottda.game.model.ProjectileListener;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.List;

/**
 * A concrete implementation for a Character Builder pattern
 */
public class ProjectileBuilder extends AbstractModelBuilder implements IProjectileBuilder {


    private int team = 0;
    private Boolean bouncy = false;
    private Boolean piercing = false;
    private List<ProjectileListener> listeners = new ArrayList<ProjectileListener>();
    private List<Boolean> ignored = null;
    private int damage = 0;
    private long lifetime = Projectile.DEFAULT_PROJECTILE_LIFETIME_MS;
    private Vector2f velocity = new Vector2f(0, 0);


    @Override
    public ModelActor create() {

        Projectile projectile = new Projectile(damage,
                listeners,
                lifetime);
        projectile.setBouncy(bouncy);
        projectile.setPiercing(piercing);
        projectile.setTeam(team);

        if(ignored != null) {
            for (int i = 0; i < ignored.size(); i++) {
                if (ignored.get(i)) {
                    projectile.ignoreListener(i);
                }
            }
        }

        projectile.setPhysicsActor(physiscActorFactory.getProjectilePhysicsActor(projectile));

        projectile.giveInput(velocity, null);

        setCommonAndNotify(projectile);
        return projectile;

    }

    // ------------ Setters ---------

    @Override
    public IProjectileBuilder copyProperties(Projectile p) {
        team = p.getTeam();
        bouncy = p.isBouncy();
        piercing = p.isPiercing();
        damage = p.getDamage();
        lifetime = p.getLifeTimeMS();
        sprite = p.getSprite();
        return this;
    }

    @Override
    public IProjectileBuilder setTeam(int n) {
        team = n;
        return this;
    }

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
    public IProjectileBuilder setListeners(List<ProjectileListener> listeners, List<Boolean> ignored) {
        this.listeners = listeners;
        this.ignored = ignored;
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
