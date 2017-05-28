package com.pottda.game.model;

import com.pottda.game.model.items.*;
import org.junit.Before;
import org.junit.Test;

import javax.vecmath.Point2i;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        a.setOrientation(2);
        a.setX(2);
        a.setY(5);

        b = new DemoItemB() {
        };
        b.setOrientation(3);
        b.setX(8);
        b.setY(4);

        bouncingBallCannon = new BouncingBallCannon();
        damageItem = new DamageItem();
        damageItem.setSize(ItemSize.NORMAL);
        enemySimpleCannon = new EnemySimpleCannon();
        healthItem = new HealthItem();
        healthItem.setSize(ItemSize.NORMAL);
        penetratingCannon = new PenetratingCannon();
        projectileSpeedItem = new ProjectileSpeedItem();
        projectileSpeedItem.setSize(ItemSize.NORMAL);
        speedItem = new SpeedItem();
        speedItem.setSize(ItemSize.NORMAL);
    }


    @Test
    public void rotateTest() {
        Set<Point2i> positionsA = (Set<Point2i>) a.getTransformedRotatedPositions();
        Set<Point2i> outputA = (Set<Point2i>) a.getTransformedRotatedOutputs();

        Set<Point2i> expectedPositionsA = new HashSet<Point2i>();
        {
            expectedPositionsA.add(new Point2i(2, 5));
            expectedPositionsA.add(new Point2i(2, 4));
            expectedPositionsA.add(new Point2i(1, 5));
            expectedPositionsA.add(new Point2i(1, 4));
        }

        Set<Point2i> expectedOutputA = new HashSet<Point2i>();
        {
            expectedOutputA.add(new Point2i(0, 4));
        }

        assertEquals(expectedPositionsA, positionsA);
        assertEquals(expectedOutputA, outputA);
    }

    @Test
    public void testBouncingBallCannon() {


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
        assertTrue(damageItem.dropRate == 0.25f * 0.5f
                || damageItem.dropRate == 0.5f * 0.5f
                || damageItem.dropRate == 0.1f * 0.5f);
        assertTrue(damageItem.name.contains("Damage"));
        assertTrue(damageItem.basePositions.size() == 2 || damageItem.basePositions.size() == 1);
        assertTrue(damageItem.isProjectileModifier);

        Projectile p = new Projectile(10, new ArrayList<ProjectileListener>());
        damageItem.onAttack(p);
        assertTrue(p.damage >= 10);
    }

    @Test
    public void testEnemySimpleCannon() {

        assertTrue(enemySimpleCannon.isPrimaryAttack);
        assertFalse(enemySimpleCannon.bounces);
        assertEquals(enemySimpleCannon.basePositions.size(), 1);
        assertEquals(enemySimpleCannon.baseOutputs.size(), 1);
        assertTrue(enemySimpleCannon.cooldownMS == 500);
        assertTrue(enemySimpleCannon.damage == 20);
    }

    @Test
    public void testHealthItem() {
        assertTrue(healthItem.statMap.containsKey(Stat.HEALTH));
        assertTrue(healthItem.dropRate == 0.25f * 0.5f
                || healthItem.dropRate == 0.5f * 0.5f
                || healthItem.dropRate == 0.1f * 0.5f);
        assertTrue(healthItem.name.contains("Health"));
    }

    @Test
    public void testPenetratingCannon() {

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
        assertTrue(projectileSpeedItem.dropRate == 0.25f * 0.5f
                || projectileSpeedItem.dropRate == 0.5f * 0.5f
                || projectileSpeedItem.dropRate == 0.1f * 0.5f);
        assertTrue(projectileSpeedItem.name.contains("P. Speed"));
        assertTrue(projectileSpeedItem.isProjectileModifier);
    }

    @Test
    public void testSpeedItem() {
        assertTrue(speedItem.statMap.containsKey(Stat.ACCEL));
        assertTrue(speedItem.dropRate == 0.25f * 0.5f
                || speedItem.dropRate == 0.5f * 0.5f
                || speedItem.dropRate == 0.1f * 0.5f);
        assertTrue(speedItem.name.contains("Speed"));
    }

}
