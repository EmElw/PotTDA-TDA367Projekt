package com.pottda.game.model;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.List;


public abstract class AttackItem extends Item {

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

    @Override
    public void init() {
        damage = 0;
        cooldown = 100;
        lastAttackTime = 0;
        super.init();
    }

    /**
     * @param direction
     * @param origin
     * @return
     */
    public List<ProjectileListener> attack(Vector2f direction, Vector2f origin) {
        List<ProjectileListener> listeners = new ArrayList<ProjectileListener>();

        Item i = this;

        // Add listeners
        while ((i = i.getNext()) != null) {
            if (i.isProjectileModifier) {
                listeners.add(i);
            }
        }

        lastAttackTime = System.currentTimeMillis();
        return listeners;
        // TODO create projectiles

    }

    public void tryAttack(Vector2f direction, Vector2f origin) {
        if (System.currentTimeMillis() - lastAttackTime > cooldown) {
            attack(direction, origin);
        }
    }
}
