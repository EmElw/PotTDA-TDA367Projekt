package com.pottda.game.model;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gustav Lahti on 2017-04-12.
 */

public class Inventory {
    public final List<AttackItem> attackItems;
    public final List<SupportItem> supportItems;
    public final List<Item> inactiveItems;
    public final List<Item> items;
    private int height;
    private int width;

    public Inventory() {
        attackItems = new ArrayList<AttackItem>();
        supportItems = new ArrayList<SupportItem>();
        inactiveItems = new ArrayList<Item>();
        items = new ArrayList<Item>();
    }

    public int getCooldown() {
        int cooldown = 0;
        for (AttackItem attackItem : attackItems) {
            cooldown += attackItem.cooldown;
        }
        return cooldown;
    }

    public int getHealth() {
        int health = 0;
        for (SupportItem supportItem : supportItems) {
            health += supportItem.health;
        }
        return health;
    }

    public float getAcceleration() {
        float acceleration = 0.0f;
        for (SupportItem supportItem : supportItems) {
            acceleration += supportItem.acceleration;
        }
        return acceleration;
    }

    public List<Projectile> getProjectile() {
        List<Projectile> projectiles = new ArrayList<Projectile>(getProjectileAmount());
        int damage = getDamage();
        List<ProjectileListener> projectileListeners = getProjectileListeners();

        for (int i = 0, n = getProjectileAmount(); i < n; i++) {
            projectiles.add(new Projectile(new PhysicsActor() {
                @Override
                public Vector2f getPosition() {
                    return null;
                }
            }));
        }

        return projectiles;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    private int getDamage() {
        int damage = 0;
        for (AttackItem attackItem : attackItems) {
            damage += attackItem.damage;
        }
        return damage;
    }

    private List<ProjectileListener> getProjectileListeners() {
        List<ProjectileListener> projectileListeners = new ArrayList<ProjectileListener>();
        for (AttackItem attackItem : attackItems) {
            if (attackItem.projectileListener != null) {
                projectileListeners.add(attackItem.projectileListener);
            }
        }
        return projectileListeners;
    }

    private int getProjectileAmount() {
        int projectileAmount = 1;
        for (AttackItem attackItem : attackItems) {
            // Need to subtract by one so multiple Items that has one Projectile each makes the Character shoot
            // multiple Projectiles.
            projectileAmount += attackItem.projectileAmount - 1;
        }
        return projectileAmount;
    }

    public void setDimensions(int w, int h) {
        this.width = w;
        this.height = h;
    }
}
