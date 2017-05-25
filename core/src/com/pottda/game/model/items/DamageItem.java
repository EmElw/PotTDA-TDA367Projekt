package com.pottda.game.model.items;

import com.pottda.game.model.Projectile;

public class DamageItem extends GenericProjectileModifier {
    private int damage;

    @Override
    protected void initDynamic() {
        damage = Math.round(10 * itemSize.getStatMultiplier());
        dropRate = 0.5f * itemSize.getDropRate();
        name = itemSize.getName() + "Damage Module";
        setBasePositions();
        isProjectileModifier = true;
    }

    @Override
    public void onAttack(Projectile p) {
        p.damage += damage;
    }
}
