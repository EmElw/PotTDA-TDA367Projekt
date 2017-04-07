package com.pottda.game;

import com.sun.xml.internal.ws.client.sei.ResponseBuilder;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Gustav Lahti on 05/04/2017.
 */
public abstract class Character {
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

    public Character(Inventory inventory, int team){
        this.inventory = inventory;
        isProjectile = false;
        this.team = team;

        // Init health
        health = baseHealth + inventory.getHealth;
        currentHealth = health;

        // Init cooldown
        cooldown = baseCooldown + item.getCooldown();
        lastAttackTime = System.currentTimeMillis() - cooldown;

        accel = baseAccel + inventory.getAccel();
    }

    // -- Public methods --

    /**
     * The character's attack
     * @param direction The direction in which the character should attack
     */
    public void attack(float direction){
        if(System.currentTimeMillis() >= lastAttackTime + cooldown){
            // TODO Attack
            // TODO Trigger animations and sound effects
            lastAttackTime = System.currentTimeMillis();
        }
    }

    public VectorType getMove(){
        VectorType v = controller.getMove();
        v.x *= accel;
        v.y *= accel;
        return v;
    }

    /**
     * Reacts to taking damage
     * @param incomingDamage Damage dealt to this character
     */
    public void takeDamage(int incomingDamage){
        currentHealth -= incomingDamage;
        if(currentHealth <= 0){
            // TODO Die
        }
    }
}
