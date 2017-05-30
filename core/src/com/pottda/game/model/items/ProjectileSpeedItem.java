package com.pottda.game.model.items;

import com.pottda.game.model.Projectile;

public class ProjectileSpeedItem extends GenericProjectileModifier {
    private float projectileSpeedMultiplier;

    @Override
    protected void initDynamic() {
        name = "P. Speed";
        isProjectileModifier = true;
    }

    @Override
    public void onAttack(Projectile p) {
        p.changeSpeed(projectileSpeedMultiplier);
    }

    @Override
    protected void setConcreteValues() {
        setColor(100 + 25 * ((int) itemSize.getStatMultiplier()), 50 + 55 * ((int) itemSize.getStatMultiplier()), 10);
        projectileSpeedMultiplier = 1f + (float) (0.1 * itemSize.getStatMultiplier());
        dropRate = 0.5f * itemSize.getDropRate();
    }
}
