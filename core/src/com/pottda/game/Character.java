package com.pottda.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.sun.xml.internal.ws.client.sei.ResponseBuilder;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Gustav Lahti on 05/04/2017.
 */
public abstract class Character {
    public List<AttackItem> attackItems;
    public List<SupportItem> supportItems;

    public final int baseHealth;
    public int itemHealth;
    public int currentHealth;

    public final int baseCooldown;
    public int itemCooldown;
    public long lastAttackTime;

    public final float baseAccel;
    public float itemAccel;

    public Body body;
    public Sprite sprite;

    // -- Constructors --

    public Character(List<AttackItem> attackItems, List<SupportItem> supportItems, Body body, Sprite sprite){
        this.attackItems = attackItems;
        this.supportItems = supportItems;
        this.body = body;
        this.sprite = sprite;

        // Init health
        baseHealth = 100;
        itemHealth = getItemHealth();
        currentHealth = itemHealth + baseHealth;

        // Init cooldown
        baseCooldown = 100;
        itemCooldown = getItemCooldown();
        lastAttackTime = System.currentTimeMillis() - baseCooldown - itemCooldown;

        baseAccel = 100;
        itemAccel = getItemAccel();
    }

    // -- Public methods --

    /**
     * Applies a force to the character causing it to accelerate
     * @param v The unit vector pointing in the direction the character should accelerate
     * @param magnitude How much of the character's total acceleration should be applied (0-1)
     */
    public void move(Vector2 v, float magnitude){
        v.set(v.x * magnitude * (baseAccel + itemAccel), v.y * magnitude * (baseAccel + itemAccel));
        body.applyForceToCenter(v, true);
    }

    /**
     * The character's attack
     * @param direction The direction in which the character should attack
     */
    public void attack(float direction){
        if(System.currentTimeMillis() >= lastAttackTime + baseCooldown + itemCooldown){
            // TODO Attack
            // TODO Trigger animations and sound effects
            lastAttackTime = System.currentTimeMillis();
        }
    }

    /**
     * Reacts to taking damage
     * @param incomingDamage Damage dealt to this character
     */
    public void takeDamage(int incomingDamage){
        currentHealth -= incomingDamage;
        if(currentHealth <= 0){
            // TODO Die
            // TODO Trigger animations and sound effects
        } else if(incomingDamage >= 50) {
            // TODO Trigger other animations and sound effects
        } else {
            // TODO Trigger other other animations and sound effects
        }
    }

    public int getMaxHealth() {
        return baseHealth + itemHealth;
    }

    // -- Private methods --

    private int getItemHealth(){
        int itemHealthTracker = 0;
        if(supportItems.size() > 0){
            for(SupportItem supportItem : supportItems){
                itemHealthTracker += supportItem.getHealth();
            }
        }
        return Math.max(itemHealthTracker, -(baseHealth - 1));
    }

    private int getItemCooldown(){
        int itemCooldownTracker = 0;
        if(attackItems.size() > 0){
            for(AttackItem attackItem : attackItems){
                itemCooldownTracker += attackItem.getCooldown();
            }
        }
        return Math.max(itemCooldownTracker, -(baseCooldown - 10));
    }

    private float getItemAccel(){
        float itemAccelTracker = 0f;
        if(supportItems.size() > 0){
            for(SupportItem supportItem : supportItems){
                itemAccelTracker += supportItem.getAccel();
            }
        }
        return Math.max(itemAccelTracker, -(baseAccel * 0.9f));
    }
}
