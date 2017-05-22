package com.pottda.game.model.items;

import com.pottda.game.model.Projectile;
import com.pottda.game.model.ProjectileListener;

public class DamageItem extends GenericProjectileModifyer implements ProjectileListener {
    int damage;

    @Override
    protected void initDynamic() {
        damage = Math.round(10 * itemSize.getStatMultiplier());
        dropRate = 0.5f * itemSize.getDropRate();
        name = itemSize.getName() + "Damage Module";
        setBasePositions();
    }

    public void onAttack(Projectile p) {
        p.damage += damage;
    }
}
