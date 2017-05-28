package com.pottda.game.model;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
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
    private Inventory inventory;
    /**
     * Current health of a character
     */
    int currentHealth = BASE_HEALTH;
    private final Map<Stat, Double> stats;
    private final Vector2f movementVector;

    private static Character player;

    private List<DeathListener> deathListeners;
    private int scoreValue;

    // -- Constructors --

    public Character() {
        this.deathListeners = new ArrayList<DeathListener>();
        this.inventory = new Inventory();
        this.isProjectile = false;
        this.movementVector = new Vector2f();

        stats = new EnumMap<Stat, Double>(Stat.class);

        updateStats();

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

    @Override
    public void update(float delta) {
        for (AttackItem i : inventory.attackItems) {
            i.decreaseCooldown(delta);
        }
    }

    private void attack(Vector2f direction) {
        inventory.attack(direction, getPosition(), getTeam());
    }

    /**
     * Reacts to taking damage
     *
     * @param incomingDamage Damage dealt to this character
     */
    void takeDamage(int incomingDamage) {
        currentHealth -= incomingDamage;
        if (currentHealth <= 0 && !isShouldBeRemoved()) {
            setShouldBeRemoved(true);
            if (deathListeners != null) {
                for (DeathListener dl : deathListeners) {
                    dl.onDeath(this);
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
        return (int) stats.get(HEALTH).floatValue();
    }

    @Override
    public void inventoryChanged() {
        int missingHealth = 0;
        // Check the missing health if currentHealth is bigger than zero
        if (currentHealth > 0) {
            int health = stats.get(HEALTH).intValue();
            if (health > 0) {
                missingHealth = health - currentHealth;
            }
        }

        updateStats();

        // Assign further as necessary
        currentHealth = (int) Math.max(Math.round(stats.get(Stat.HEALTH) - missingHealth), 1);
    }

    private void updateStats() {

        // Fetch all the stats from the inventory
        for (Stat stat : Stat.values()) {
            stats.put(stat, 0 + inventory.getStatSum(stat));
        }

        // Add base values
        stats.put(Stat.HEALTH, stats.get(Stat.HEALTH) + (double) BASE_HEALTH);
        stats.put(ACCEL, stats.get(ACCEL) + (double) BASE_ACCEL);
    }

    public void setDeathListeners(List<DeathListener> deathListeners) {
        this.deathListeners = deathListeners;
    }

    public void addDeathListener(DeathListener listener) {
        deathListeners.add(listener);
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(int scoreValue) {
        this.scoreValue = scoreValue;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public static Character getPlayer() {
        return player;
    }

    public static void setPlayer(Character playerCharacter) {
        player = playerCharacter;
    }
}
