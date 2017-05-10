package com.pottda.game.model;

import javax.vecmath.Vector2f;
import java.util.EnumMap;
import java.util.Map;


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
    public int currentHealth;
    private static Map<Stat, Double> stats;


    // -- Constructors --

    public Character(PhysicsActor physicsActor) {
        super(physicsActor);
        this.inventory = new Inventory();
        this.isProjectile = false;
        // this.team = 0;

        stats = new EnumMap<Stat, Double>(Stat.class);

        // Sum all simple stats
        for (Stat stat : Stat.values()) {
            stats.put(stat, 0 + inventory.getSumStat(stat));
        }
        // Add base values
        stats.put(Stat.HEALTH, stats.get(Stat.HEALTH) + (double) BASE_HEALTH);
        stats.put(Stat.ACCEL, stats.get(Stat.ACCEL)+ (double) BASE_ACCEL);

        // Assign further as necessary
        this.currentHealth = stats.get(Stat.HEALTH).intValue();


    }

    @Override
    public void giveInput(Vector2f move, Vector2f attack) {
        // Movement
        move.set(move.x * stats.get(Stat.ACCEL).floatValue(),
                move.y * stats.get(Stat.ACCEL).floatValue());
        physicsActor.giveMovementVector(move);
        this.angle = (float) Math.toDegrees(Math.atan2(-attack.getY(), attack.getX()));
        attack(attack);
    }

    private void attack(Vector2f direction) {
        inventory.attack(direction, getPosition());
    }

    @Override
    public float getAngle() {
        if (team == 1) { // Fix rotation for enemies so they rotate towards player
            return 0 - super.getAngle();
        }
        return super.getAngle();
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
