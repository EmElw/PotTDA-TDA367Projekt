package com.pottda.game.model;

import javax.vecmath.Vector2f;


public class Character extends ModelActor {
    /**
     * Base maxHealth of any character, further modified by its Inventory
     */
    private static final int BASE_HEALTH = 100;
    private static final float baseAccel = 100;
    /**
     * A reference to the character's inventory, should be effectively final
     */
    public Inventory inventory;
    /**
     * Maxiumum health of a character
     */
    public int maxHealth;
    /**
     * Current health of a character
     */
    public int currentHealth;
    /**
     * Acceleration from movement input. Effectively max size of vector
     */
    public float accel;


    // -- Constructors --

    public Character(PhysicsActor physicsActor) {
        super(physicsActor);
        this.inventory = new Inventory();
        this.isProjectile = false;
        this.team = 0;

        // Init maxHealth
        maxHealth = BASE_HEALTH + inventory.getHealth();
        currentHealth = maxHealth;

        accel = baseAccel + inventory.getAcceleration();
    }

    @Override
    public void giveInput(Vector2f move, Vector2f attack) {
        // Movement
        move.set(move.x * accel, move.y * accel);
        physicsActor.giveMovementVector(move);

        attack(attack);
    }

    private void attack(Vector2f direction) {
        inventory.attack(direction, getPosition());
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

//    private void setProjectileMovement(List<Projectile> projectiles, Vector2f attack) {
//        Vector2f temp;
//        for (int i = 0, n = projectiles.size(); i < n; i++) {
//            temp = rotateVector(attack, PROJECTILE_ANGLE * ((n / 2f) - (float) i));
//            temp.normalize();
//            projectiles.get(i).giveInput(temp, null);
//        }
//    }
//
//    private Vector2f rotateVector(Vector2f vector, float rad) {
//        return new Vector2f(vector.x * (float) Math.cos((double) rad),
//                vector.y * (float) Math.sin((double) rad));
//    }
}