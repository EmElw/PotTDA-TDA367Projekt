package com.pottda.game.model;

import com.pottda.game.model.items.BouncingBallCannon;
import com.pottda.game.model.items.DamageItem;
import com.pottda.game.model.items.EnemySimpleCannon;
import com.pottda.game.model.items.GenericProjectileModifier;
import com.pottda.game.model.items.HealthItem;
import com.pottda.game.model.items.PenetratingCannon;
import com.pottda.game.model.items.ProjectileSpeedItem;
import com.pottda.game.model.items.SpeedItem;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class MoreItemsTests {

    private AttackItem bouncingBallCannon;
    private GenericProjectileModifier damageItem;
    private Item enemySimpleCannon;
    private Item healthItem;
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
        assertTrue(!bouncingBallCannon.bounces);
        assertEquals(bouncingBallCannon.basePositions, null);
        assertEquals(bouncingBallCannon.baseOutputs, null);
        assertFalse(bouncingBallCannon.cooldownMS == 300);
        assertFalse(bouncingBallCannon.damage == 10);

        bouncingBallCannon.init();

        assertTrue(bouncingBallCannon.isPrimaryAttack);
        assertFalse(!bouncingBallCannon.bounces);
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

}
