package com.pottda.game.model;

import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * Created by Gustav Lahti on 2017-04-20.
 */
public class InventoryTest {
    private Inventory inventory = new Inventory(new ArrayList<AttackItem>(), new ArrayList<SupportItem>(), null);
    private Random random = new Random();

    @After
    public void tearDown() throws Exception {
        inventory.attackItems = null;
        inventory.supportItems = null;
        inventory.inactiveItems = null;
    }

    @Test
    public void getCooldown() throws Exception {
        int cooldownInput = 0;
        int tempCooldown;

        for (int i = 0; i < 10; i++){
            tempCooldown = random.nextInt(51) - 25;
            cooldownInput += tempCooldown;
            inventory.attackItems.add(new AttackItem(0, tempCooldown, 1, null));
        }

        assertTrue(inventory.getCooldown() == cooldownInput);
    }

    @Test
    public void getHealth() throws Exception {
        int healthInput = 0;
        int tempHealth;

        for (int i = 0; i < 10;i++){
            tempHealth = random.nextInt(51);
            healthInput += tempHealth;
            inventory.supportItems.add(new SupportItem(tempHealth, 0f));
        }

        assertTrue(inventory.getHealth() == healthInput);
    }

    @Test
    public void getAcceleration() throws Exception {
        float accelerationInput = 0f;
        float tempAcceleration;

        for (int i = 0; i < 10; i++){
            tempAcceleration = random.nextFloat();
            accelerationInput += tempAcceleration;
            inventory.supportItems.add(new SupportItem(0, tempAcceleration));
        }

        assertTrue(Math.abs(inventory.getAcceleration() - accelerationInput) <= 0.01f);
    }

    @Test
    public void getProjectile() throws Exception {
        int damageInput = 0;
        int projectileAmountInput = 1;

        int tempDamage;
        int tempProjectileAmount;

        List<Projectile> projectiles;

        for (int i = 0; i < 10; i++){
            tempDamage = random.nextInt(50);
            tempProjectileAmount = random.nextInt(3);

            damageInput += tempDamage;
            projectileAmountInput += tempProjectileAmount;

            inventory.attackItems.add(new AttackItem(tempDamage, 0, 1 + tempProjectileAmount, null));
        }

        projectiles = inventory.getProjectile();

        assertTrue(projectiles.size() == projectileAmountInput);
        assertTrue(projectiles.get(0).damage == damageInput);
    }

}