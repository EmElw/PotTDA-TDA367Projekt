package com.pottda.game.model;

import com.pottda.game.model.items.BouncingBallCannon;
import com.pottda.game.model.items.DamageItem;
import com.pottda.game.model.items.DemoItemA;
import com.pottda.game.model.items.DemoItemB;
import com.pottda.game.model.items.EnemySimpleCannon;
import com.pottda.game.model.items.GenericProjectileModifier;
import com.pottda.game.model.items.HealthItem;
import com.pottda.game.model.items.PenetratingCannon;
import com.pottda.game.model.items.ProjectileSpeedItem;
import com.pottda.game.model.items.SpeedItem;
import com.pottda.game.model.items.SupportItem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test the spatial properties of the Item class
 */
public class ItemTest {

    private Item a;
    private Item b;

    private AttackItem bouncingBallCannon;
    private GenericProjectileModifier damageItem;
    private AttackItem enemySimpleCannon;
    private SupportItem healthItem;
    private AttackItem penetratingCannon;
    private GenericProjectileModifier projectileSpeedItem;
    private SupportItem speedItem;

    @Before
    public void setUp() {
        a = new DemoItemA() {
        };
        a.init();
        a.setOrientation(2);
        a.setX(2);
        a.setY(5);

        b = new DemoItemB() {
        };
        b.init();
        b.setOrientation(3);
        b.setX(8);
        b.setY(4);

        bouncingBallCannon = new BouncingBallCannon();
        damageItem = new DamageItem();
        enemySimpleCannon = new EnemySimpleCannon();
        healthItem = new HealthItem();
        penetratingCannon = new PenetratingCannon();
        projectileSpeedItem = new ProjectileSpeedItem();
        speedItem = new SpeedItem();
    }

    @Test
    public void rotateTest() {

        List<Integer> positionsA = a.getPositionsAsIntegers(10);
        List<Integer> outputA = a.getOutputAsInteger(10);
        Collections.sort(positionsA);

        List<Integer> positionsB = b.getPositionsAsIntegers(10);
        List<Integer> outputB = b.getOutputAsInteger(10);
        Collections.sort(positionsB);

        // Create the expected values
        List<Integer> rotatedA = new ArrayList<Integer>();
        rotatedA.addAll(Arrays.asList(51, 52, 41, 42));
        Collections.sort(rotatedA);

        List<Integer> rotatedB = new ArrayList<Integer>();
        rotatedB.addAll(Arrays.asList(48, 38, 39, 29));
        Collections.sort(rotatedB);

        Assert.assertEquals(rotatedA, positionsA);
        Assert.assertEquals(rotatedB, positionsB);
        Assert.assertEquals(outputA.get(0), Integer.valueOf(40));
        Assert.assertEquals(outputB.get(0), Integer.valueOf(19));
    }

    @Test
    public void testBouncingBallCannon() {
        assertFalse(bouncingBallCannon.isPrimaryAttack);
        assertFalse(bouncingBallCannon.bounces);
        assertEquals(bouncingBallCannon.basePositions, null);
        assertEquals(bouncingBallCannon.baseOutputs, null);
        assertFalse(bouncingBallCannon.cooldownMS == 300);
        assertFalse(bouncingBallCannon.damage == 10);

        bouncingBallCannon.init();

        assertTrue(bouncingBallCannon.isPrimaryAttack);
        assertTrue(bouncingBallCannon.bounces);
        assertEquals(bouncingBallCannon.basePositions.size(), 2);
        assertEquals(bouncingBallCannon.baseOutputs.size(), 1);
        assertTrue(bouncingBallCannon.cooldownMS == 300);
        assertTrue(bouncingBallCannon.damage == 10);

        bouncingBallCannon.setX(2);
        bouncingBallCannon.setY(3);
        bouncingBallCannon.setOrientation(1);
        assertTrue(bouncingBallCannon.getX() == 2);
        assertTrue(bouncingBallCannon.getY() == 3);
        assertTrue(bouncingBallCannon.getOrientation() == 1);
    }

    @Test
    public void testDamageItem() {
        // Drop rate: 0.25f, 0.5f, 0.1f
        damageItem.init();
        assertTrue(damageItem.dropRate == 0.25f * 0.5f
                || damageItem.dropRate == 0.5f * 0.5f
                || damageItem.dropRate == 0.1f * 0.5f);
        assertTrue(damageItem.name.contains("Damage Module"));
        assertTrue(damageItem.basePositions.size() == 2 || damageItem.basePositions.size() == 1);
        assertTrue(damageItem.isProjectileModifier);

        Projectile p = new Projectile(10, new ArrayList<ProjectileListener>());
        damageItem.onAttack(p);
        assertTrue(p.damage >= 10);
    }

    @Test
    public void testEnemySimpleCannon() {
        assertFalse(enemySimpleCannon.isPrimaryAttack);
        assertFalse(enemySimpleCannon.bounces);
        assertEquals(enemySimpleCannon.basePositions, null);
        assertEquals(enemySimpleCannon.baseOutputs, null);
        assertFalse(enemySimpleCannon.cooldownMS == 500);
        assertFalse(enemySimpleCannon.damage == 20);

        enemySimpleCannon.init();

        assertTrue(enemySimpleCannon.isPrimaryAttack);
        assertFalse(enemySimpleCannon.bounces);
        assertEquals(enemySimpleCannon.basePositions.size(), 1);
        assertEquals(enemySimpleCannon.baseOutputs.size(), 1);
        assertTrue(enemySimpleCannon.cooldownMS == 500);
        assertTrue(enemySimpleCannon.damage == 20);
    }

    @Test
    public void testHealthItem() {
        healthItem.init();
        assertTrue(healthItem.statMap.containsKey(Stat.HEALTH));
        assertTrue(healthItem.dropRate == 0.25f * 0.75f
                || healthItem.dropRate == 0.5f * 0.75f
                || healthItem.dropRate == 0.1f * 0.75f);
        assertTrue(healthItem.name.contains("Health Module"));
    }

    @Test
    public void testPenetratingCannon() {
        assertFalse(penetratingCannon.isPrimaryAttack);
        assertFalse(penetratingCannon.piercing);
        assertFalse(penetratingCannon.bounces);
        assertEquals(penetratingCannon.basePositions, null);
        assertEquals(penetratingCannon.baseOutputs, null);
        assertFalse(penetratingCannon.cooldownMS == 300);
        assertFalse(penetratingCannon.damage == 10);

        penetratingCannon.init();

        assertTrue(penetratingCannon.isPrimaryAttack);
        assertTrue(penetratingCannon.piercing);
        assertFalse(penetratingCannon.bounces);
        assertEquals(penetratingCannon.basePositions.size(), 2);
        assertEquals(penetratingCannon.baseOutputs.size(), 1);
        assertTrue(penetratingCannon.cooldownMS == 300);
        assertTrue(penetratingCannon.damage == 10);
    }

    @Test
    public void testProjectileSpeedItem() {
        projectileSpeedItem.init();
        assertTrue(projectileSpeedItem.dropRate == 0.25f * 0.5f
                || projectileSpeedItem.dropRate == 0.5f * 0.5f
                || projectileSpeedItem.dropRate == 0.1f * 0.5f);
        assertTrue(projectileSpeedItem.name.contains("Projectile Speed Module"));
        assertTrue(projectileSpeedItem.isProjectileModifier);
    }

    @Test
    public void testSpeedItem() {
        speedItem.init();
        assertTrue(speedItem.statMap.containsKey(Stat.ACCEL));
        assertTrue(speedItem.dropRate == 0.25f * 0.5f
                || speedItem.dropRate == 0.5f * 0.5f
                || speedItem.dropRate == 0.1f * 0.5f);
        assertTrue(speedItem.name.contains("Speed Module"));
    }

}
