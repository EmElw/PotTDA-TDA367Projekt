package com.pottda.game.model;

import com.pottda.game.controller.Box2DActorFactory;
import com.pottda.game.view.Sprites;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.List;


public abstract class AttackItem extends Item {
    private final static float CHARACTER_RADIUS = 0.5f;

    /**
     * The base damage of any {@link Projectile} created by this Item
     */
    protected int damage;

    /**
     * The cool down, measured in milliseconds, before this Item can fire
     */
    protected int cooldown;

    /**
     * The last time an item attacked, measured in milliseconds.
     */
    private long lastAttackTime;

    protected boolean bounces;
    protected boolean penetrates;

    @Override
    public void init() {
        damage = 0;
        cooldown = 100;
        lastAttackTime = 0;
        bounces = false;
        penetrates = false;
        isPrimaryAttack = false;
        isProjectileModifier = false;
        isSecondaryAttack = false;
        super.init();
    }

    /**
     * @param direction
     * @param origin
     * @return
     */
    public List<ProjectileListener> attack(Vector2f direction, Vector2f origin, int team) {
        List<ProjectileListener> listeners = new ArrayList<ProjectileListener>();

        Item i = this;

        // Add listeners
        while ((i = i.getNext()) != null) {
            if (i.isProjectileModifier) {
                listeners.add(i);
            }
        }

        direction.scale(CHARACTER_RADIUS);
        origin.add(direction);
        direction.normalize();

        Projectile proj = (Projectile) ActorFactory.get().buildProjectile(
                Sprites.PROJECTILE1,
                team,
                bounces,
                penetrates,
                origin).getModel();

        proj.damage = damage;

        direction.normalize();
        proj.setListeners(listeners);
        proj.giveInput(direction, null);
        proj.onAttack();

        lastAttackTime = System.currentTimeMillis();
        return listeners;

    }

    public void tryAttack(Vector2f direction, Vector2f origin, int team) {
        if (System.currentTimeMillis() - lastAttackTime > cooldown) {
            attack(direction, origin, team);
        }
    }
}
