package com.pottda.game.Model;

/**
 * Created by rikar on 2017-04-07.
 */

public class AttackItem extends Item {
    int damage;
    int cooldown;
    int projectileAmount;
    ProjectileListener projectileListener;

    public AttackItem(int damage, int cooldown, int projectileAmount, ProjectileListener projectileListener) {
        this.damage = damage;
        this.cooldown = cooldown;
        this.projectileAmount = projectileAmount;
        this.projectileListener = projectileListener;
    }
}
