package com.pottda.game.model.items;

import com.pottda.game.model.Projectile;

public class ProjectileSpeedItem extends GenericProjectileModifier {
    private float projectileSpeedMultiplier;

    @Override
    protected void initDynamic() {
        projectileSpeedMultiplier = 1f + (float)(0.1 * itemSize.getStatMultiplier());
        dropRate = 0.5f * itemSize.getDropRate();
        name = "P. Speed";
        isProjectileModifier = true;
    }

    @Override
    public void onAttack(Projectile p) {
        p.changeSpeed(projectileSpeedMultiplier);
    }
}
