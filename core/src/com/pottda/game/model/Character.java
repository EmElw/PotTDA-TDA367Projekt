package com.pottda.game.model;

import javax.vecmath.Vector2f;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.pottda.game.model.Stat.ACCEL;
import static com.pottda.game.model.Stat.HEALTH;


public class Character extends ModelActor implements InventoryChangeListener {
    /**
     * Base maxHealth of any character, further modified by its Inventory
     */
    private static final int BASE_HEALTH = 100;
    private static final float BASE_ACCEL = 30f;
    /**
     * A reference to the character's inventory, should be effectively final
     */
    public Inventory inventory;
    /**
     * Current health of a character
     */
    int currentHealth = 0;
    private final Map<Stat, Double> stats;
    private final Vector2f movementVector;

    public static Character player;

    private List<DeathListener> deathListeners;

    // -- Constructors --

    public Character() {
        this.inventory = new Inventory();
        this.isProjectile = false;
        this.movementVector = new Vector2f();

        stats = new EnumMap<Stat, Double>(Stat.class);

//        // Sum all simple stats
//        for (Stat stat : Stat.values()) {
//            stats.put(stat, 0 + inventory.getSumStat(stat));
//        }

        inventoryChanged();
    }

    @Override
    public void giveInput(Vector2f move, Vector2f attack) {
        // TODO Ta reda på varför det är en annan stats Map när denna kallas jämfört med när stats uppdateras från inventory
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

    /**
     * Reacts to taking damage
     *
     * @param incomingDamage Damage dealt to this character
     */
    void takeDamage(int incomingDamage) {
        currentHealth -= incomingDamage;
        if (currentHealth <= 0 && !shouldBeRemoved) {
            shouldBeRemoved = true;
            if (deathListeners != null) {
                for (DeathListener dl : deathListeners) {
                    dl.onDeath(inventory.getItemDropList());
                }
            }
        }
    }

    /**
     * Returns the current health of the actor
     *
     * @return the health of the actor
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return (int)stats.get(HEALTH).floatValue();
    }

    @Override
    public void inventoryChanged() {
        double healthFraction = 1;
        // Check the missing health if currentHealth is bigger than zero
        if (currentHealth > 0) {
            healthFraction = currentHealth / stats.get(HEALTH).intValue();
        }

        // Fetch all the stats from the inventory
        for (Stat stat : Stat.values()) {
            stats.put(stat, 0 + inventory.getSumStat(stat));
        }

        // Add base values
        stats.put(Stat.HEALTH, stats.get(Stat.HEALTH) + (double) BASE_HEALTH);
        stats.put(ACCEL, stats.get(ACCEL) + (double) BASE_ACCEL);

        // Assign further as necessary
        currentHealth = (int) Math.max(Math.round(stats.get(Stat.HEALTH) * healthFraction), 1);
    }

    public void setDeathListeners(List<DeathListener> deathListeners) {
        this.deathListeners = deathListeners;
    }
}
