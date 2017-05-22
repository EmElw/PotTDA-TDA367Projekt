package com.pottda.game.model.items;

public class DamageItem extends GenericProjectileModifyer {
    @Override
    protected void initDynamic() {
        damage = Math.round(10 * itemSize.getStatMultiplier());
        dropRate = 0.5f * itemSize.getDropRate();
        name = itemSize.getName() + "Damage Module";
        setBasePositions();
    }
}
