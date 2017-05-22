package com.pottda.game.model.items;

import com.pottda.game.model.Projectile;
import com.pottda.game.model.ProjectileListener;
import com.pottda.game.model.builders.ProjectileBuilder;

public class ProjectileSpeedItem extends GenericProjectileModifier implements ProjectileListener {
    float projectileSpeedMultiplier;

    @Override
    protected void initDynamic() {
        projectileSpeedMultiplier = 1f + (float)(0.1 * itemSize.getStatMultiplier());
        dropRate = 0.5f * itemSize.getDropRate();
        name = itemSize.getName() + "Projectile Speed Module";
        setBasePositions();
    }

    public void onAttack(Projectile p) {
        p.changeSpeed(projectileSpeedMultiplier);
    }
}
