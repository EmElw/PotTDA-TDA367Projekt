package com.pottda.game.model;

/**
 * Created by rikar on 2017-04-07.
 */

public abstract class AttackItem extends Item {
    int damage;
    int cooldown;
    int projectileAmount;
    ProjectileListener projectileListener;

    public AttackItem() {
    }

    public AttackItem(int damage, int cooldown, int projectileAmount, ProjectileListener projectileListener) {
        this.damage = damage;
        this.cooldown = cooldown;
        this.projectileAmount = projectileAmount;
        this.projectileListener = projectileListener;
    }
}
