package com.pottda.game.model;

import javax.vecmath.Vector2f;

/**
 * Created by Gustav Lahti on 2017-04-07.
 */
public class Character extends ModelActor {
    public Inventory inventory;

    private static final int baseHealth = 100;
    public int health;
    public int currentHealth;

    private static final int baseCooldown = 100;
    public int cooldown;
    public long lastAttackTime;

    private static final float baseAccel = 100;
    public float accel;


    // -- Constructors --

    public Character() {
        this(new Inventory(), 0);
    }

    public Character(Inventory inventory, int team) {
        this.inventory = inventory;
        isProjectile = false;
        this.team = team;

        // Init health
        health = baseHealth + inventory.getHealth();
        currentHealth = health;

        // Init cooldown
        cooldown = baseCooldown + inventory.getCooldown();
        lastAttackTime = System.currentTimeMillis() - cooldown;

        accel = baseAccel + inventory.getAcceleration();
    }

    // -- Public methods --

    /**
     * The character's attack
     *
     * @param direction The direction in which the character should attack
     */
    public void attack(float direction) {
        if (System.currentTimeMillis() >= lastAttackTime + cooldown) {
            // TODO Attack, Trigger animations and sound effects
            lastAttackTime = System.currentTimeMillis();
        }
    }

    /**
     * Reacts to taking damage
     *
     * @param incomingDamage Damage dealt to this character
     */
    public void takeDamage(int incomingDamage) {
        currentHealth -= incomingDamage;
        if (currentHealth <= 0) {
            // TODO Die
        }
    }

    @Override
    public void giveInput(Vector2f move, Vector2f attack) {
        // TODO implement
    }
}
