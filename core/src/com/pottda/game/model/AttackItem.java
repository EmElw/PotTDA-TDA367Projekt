package com.pottda.game.model;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.List;


public abstract class AttackItem extends Item {

    /**
     * The base damage of any {@link Projectile} created by this Item
     */
    int damage;
    /**
     * The cool down, measured in milliseconds, before this Item can fire
     */
    int cooldown;

    @Override
    public void init() {
        damage = 0;
        cooldown = 100;
        super.init();
    }

    /**
     * Executes an attack in the given direction
     *
     * @param direction
     */
    public void attack(Vector2f direction, Vector2f origin) {

        List<ProjectileListener> listeners = new ArrayList<ProjectileListener>();

        Item i = this;

        // Add listeners
        while ((i = i.getNext()) != null) {
            if (i.isProjectileModifier) {
                listeners.add(i);
            }
        }

        // TODO create projectiles
    }
}
