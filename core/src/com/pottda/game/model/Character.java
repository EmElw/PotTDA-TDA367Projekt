package com.pottda.game.model;

import javax.vecmath.Vector2f;
import java.util.EnumMap;
import java.util.Map;


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
     * Current health of a character
     */
    public int currentHealth;
    private static Map<Stat, Double> stats;


    // -- Constructors --

    public Character(PhysicsActor physicsActor) {
        super(physicsActor);
        this.inventory = new Inventory();
        this.isProjectile = false;
        this.team = 0;

        stats = new EnumMap<Stat, Double>(Stat.class);

        // Sum all simple stats
        for (Stat stat : stats.keySet()) {
            stats.put(stat, inventory.getSumStat(stat));
        }
        // Add base values
        stats.put(Stat.HEALTH, /*stats.get(Stat.HEALTH) +*/(double) BASE_HEALTH);

        // Assign further as necessary
        this.currentHealth = stats.get(Stat.HEALTH).intValue();

    }

    @Override
    public void giveInput(Vector2f move, Vector2f attack) {
        // Movement
        move.set(move.x /* * stats.get(Stat.ACCEL).floatValue()*/,
                move.y /* * stats.get(Stat.ACCEL).floatValue()*/);
        physicsActor.giveMovementVector(move);
        this.angle = (float)Math.toDegrees(Math.atan2(-attack.getY(), attack.getX()));
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

    /*@Override
    public float getAngle() {
        if (ControllerOptions.controllerSettings == ControllerOptions.KEYBOARD_ONLY) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                return 180;
            } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                return 90;
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                return 270;
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.UP)) {
                return (180 + 90) / 2;
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                return (180 + 270) / 2;
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.UP)) {
                return 90 / 2;
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                return (270 + 360) / 2;
            }
        } else {
            return (float) Math.toDegrees(Math.atan2(physicsActor.getPosition().getY() - Gdx.input.getY(), physicsActor.getPosition().getX() - Gdx.input.getX()));
        }
        return 0;
    }*/


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
