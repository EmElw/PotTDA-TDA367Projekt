package com.pottda.game.model;

import javax.vecmath.Vector2f;
import java.util.List;

/**
 * Created by Gustav Lahti on 2017-04-07.
 */
public class Character extends ModelActor {
    public final static float PROJECTILE_ANGLE = 0.3f;

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

    public Character(PhysicsActor physicsActor) {
        super(physicsActor);
        this.inventory = new Inventory();
        this.isProjectile = false;
        this.team = 0;

        // Init health
        health = baseHealth + inventory.getHealth();
        currentHealth = health;

        // Init cooldown
        cooldown = baseCooldown + inventory.getCooldown();
        lastAttackTime = System.currentTimeMillis() - cooldown;

        accel = baseAccel + inventory.getAcceleration();
    }

    @Override
    public void giveInput(Vector2f move, Vector2f attack) {
        // Movement
        move.set(move.x * accel, move.y * accel);
        physicsActor.giveMovementVector(move);

        attack(attack);
    }

    /**
     * The character's attack
     *
     * @param direction The direction in which the character should attack
     */
    public void attack(Vector2f direction) {
        if (System.currentTimeMillis() >= lastAttackTime + cooldown) {
            lastAttackTime = System.currentTimeMillis();

            // Attack
            List<Projectile> projectiles = inventory.getProjectile();
            setProjectileMovement(projectiles, direction);
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

    private void setProjectileMovement(List<Projectile> projectiles, Vector2f attack){
        Vector2f temp;
        for (int i = 0, n = projectiles.size(); i < n; i++){
            temp = rotateVector(attack, PROJECTILE_ANGLE * ((n / 2f) - (float)i));
            temp.normalize();
            projectiles.get(i).giveInput(temp,null);
        }
    }

    private Vector2f rotateVector(Vector2f vector, float rad){
        return new Vector2f(vector.x * (float)Math.cos((double) rad),
                vector.y * (float)Math.sin((double) rad));
    }
}
