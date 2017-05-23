package com.pottda.game.model;

import com.pottda.game.model.builders.ProjectileBuilder;

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
    protected boolean piercing;

    @Override
    public void init() {
        damage = 0;
        cooldown = 100;
        lastAttackTime = 0;
        bounces = false;
        piercing = false;
        isPrimaryAttack = false;
        isProjectileModifier = false;
        isSecondaryAttack = false;
        super.init();
    }

    List<ProjectileListener> attack(Vector2f velocity, Vector2f origin, int team) {
        List<ProjectileListener> listeners = new ArrayList<ProjectileListener>();

        Item i = this;

        // Add listeners
        while ((i = i.getNext()) != null) {
            if (i.isProjectileModifier) {
                listeners.add(i);
            }
        }

        Vector2f temporaryVector;
        temporaryVector = new Vector2f(velocity);
        temporaryVector.normalize();
        temporaryVector.scale(CHARACTER_RADIUS);
        origin.add(temporaryVector);

        Sprites sprite;

        // Set sprite depending on team
        if (team == Character.PLAYER_TEAM) {
            sprite = Sprites.PLAYERPROJECTILE;
        } else {
            sprite = Sprites.ENEMYPROJECTILE;
        }

        Projectile proj = (Projectile) new ProjectileBuilder().
                setTeam(team).
                setBouncy(bounces).
                setPiercing(piercing).
                setDamage(damage).
                setVelocity(velocity).
                setListeners(listeners).
                setPosition(origin).
                setSprite(sprite).
                create();

        // Call the event
        proj.onAttack();

        lastAttackTime = System.currentTimeMillis();
        return listeners;

    }

    void tryAttack(Vector2f direction, Vector2f origin, int team) {
        if (System.currentTimeMillis() - lastAttackTime > cooldown) {
            attack(direction, origin, team);
        }
    }
}
