package com.pottda.game.model;

import com.pottda.game.model.builders.ProjectileBuilder;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.List;


public abstract class AttackItem extends Item {
    private final static float CHARACTER_RADIUS = 0.5f;

    protected boolean piercing;
    protected int cooldownMS;
    protected boolean bounces;
    protected int damage;

    private float currentCooldown;

    @Override
    public void init() {
        damage = 0;
        cooldownMS = 100;
        bounces = false;
        piercing = false;
        isPrimaryAttack = false;
        isProjectileModifier = false;
        isSecondaryAttack = false;
        super.init();
    }

    List<ProjectileListener> attack(Vector2f velocity, Vector2f origin, int team) {
        currentCooldown = cooldownMS;
        List<ProjectileListener> listeners = new ArrayList<ProjectileListener>();

        Item i = this;

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

        com.pottda.game.assets.Sprites sprite;

        if (team == Character.PLAYER_TEAM) {
            sprite = com.pottda.game.assets.Sprites.PLAYERPROJECTILE;
        } else {
            sprite = com.pottda.game.assets.Sprites.ENEMYPROJECTILE;
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

        proj.onAttack();

        return listeners;

    }

    void tryAttack(Vector2f direction, Vector2f origin, int team) {
        if (currentCooldown <= 0) {
            attack(direction, origin, team);
        }
    }

    void decreaseCooldown(float delta) {
        currentCooldown -= delta * 1000;
    }
}
