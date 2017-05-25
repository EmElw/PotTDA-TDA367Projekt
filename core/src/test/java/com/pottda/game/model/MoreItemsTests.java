package com.pottda.game.model;

import com.pottda.game.model.items.BouncingBallCannon;
import com.pottda.game.model.items.DamageItem;
import com.pottda.game.model.items.EnemySimpleCannon;
import com.pottda.game.model.items.GenericProjectileModifier;
import com.pottda.game.model.items.HealthItem;
import com.pottda.game.model.items.PenetratingCannon;
import com.pottda.game.model.items.ProjectileSpeedItem;
import com.pottda.game.model.items.SpeedItem;
import com.pottda.game.model.items.SupportItem;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class MoreItemsTests {

    private AttackItem bouncingBallCannon;
    private GenericProjectileModifier damageItem;
    private AttackItem enemySimpleCannon;
    private SupportItem healthItem;
    private Item penetratingCannon;
    private Item projectileSpeedItem;
    private Item speedItem;

    @Before
    public void init() {
        bouncingBallCannon = new BouncingBallCannon();
        damageItem = new DamageItem();
        enemySimpleCannon = new EnemySimpleCannon();
        healthItem = new HealthItem();
        penetratingCannon = new PenetratingCannon();
        projectileSpeedItem = new ProjectileSpeedItem();
        speedItem = new SpeedItem();
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
        assertFalse(enemySimpleCannon.cooldownMS == 300);
        assertFalse(enemySimpleCannon.damage == 10);

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

}
