package com.pottda.game.model.items;

import com.pottda.game.model.Projectile;

public class DamageItem extends GenericProjectileModifier {
    private int damage;

    @Override
    protected void initDynamic() {
        name = "Damage";
        isProjectileModifier = true;
    }

    @Override
    public void onAttack(Projectile p) {
        p.damage += damage;
    }

    @Override
    protected void setConcreteValues() {
        damage = Math.round(10 * itemSize.getStatMultiplier());
        dropRate = 0.5f * itemSize.getDropRate();
        setColor((int) (100 + 25 * itemSize.getStatMultiplier()), 40, 40);
    }
}
