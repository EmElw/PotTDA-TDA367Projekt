package com.pottda.game.model;

import javax.vecmath.Vector2f;

import java.util.EnumMap;
import java.util.Map;

import static com.pottda.game.model.Stat.ACCEL;


public class Character extends ModelActor {
    /**
     * Base maxHealth of any character, further modified by its Inventory
     */
    private static final int BASE_HEALTH = 100;
    private static final float BASE_ACCEL = 5f;
    /**
     * A reference to the character's inventory, should be effectively final
     */
    public Inventory inventory;
    /**
     * Current health of a character
     */
    int currentHealth;
    private static Map<Stat, Double> stats;
    private Vector2f movementVector;

    public static Character player;

    // -- Constructors --

    public Character(PhysicsActor physicsActor) {
        super(physicsActor);
        this.inventory = new Inventory();
        this.isProjectile = false;
        this.movementVector = new Vector2f();

        stats = new EnumMap<Stat, Double>(Stat.class);

        // Sum all simple stats
        for (Stat stat : Stat.values()) {
            stats.put(stat, 0 + inventory.getSumStat(stat));
        }
        // Add base values
        stats.put(Stat.HEALTH, stats.get(Stat.HEALTH) + (double) BASE_HEALTH);
        stats.put(ACCEL, stats.get(ACCEL) + (double) BASE_ACCEL);

        // Assign further as necessary
        this.currentHealth = stats.get(Stat.HEALTH).intValue();


    }

    @Override
    public void giveInput(Vector2f move, Vector2f attack) {
        // Movement
        movementVector.set(move);
        // Scale the vector based on the Character's capabilities
        movementVector.scale(stats.get(ACCEL).floatValue());

        physicsActor.giveMovementVector(movementVector);
        if (attack.length() != 0) {
            attack(attack);
            this.angle = (float) Math.toDegrees(Math.atan2(attack.getY(), attack.getX()));
        }
    }

    private void attack(Vector2f direction) {
        inventory.attack(direction, getPosition(), team);
    }

    @Override
    public float getAngle() {
        return super.getAngle();
    }

    /**
     * Reacts to taking damage
     *
     * @param incomingDamage Damage dealt to this character
     */
    void takeDamage(int incomingDamage) {
        currentHealth -= incomingDamage;
        if (currentHealth <= 0) {
            shouldBeRemoved = true;
        }
    }

    public int getCurrentHealth() {
        return currentHealth;
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
