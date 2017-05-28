package com.pottda.game.model.items;

import com.pottda.game.model.Stat;

public class HealthItem extends SupportItem{
    @Override
    protected void initDynamic() {
        statMap.put(Stat.HEALTH, 50.0 * itemSize.getStatMultiplier());
        statMap.put(Stat.ACCEL, -2.5 * itemSize.getStatMultiplier());
        dropRate = 0.5f * itemSize.getDropRate();
        name = "Health";
    }
}
