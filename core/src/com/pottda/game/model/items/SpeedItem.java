package com.pottda.game.model.items;

import com.pottda.game.model.Stat;

public class SpeedItem extends SupportItem {
    @Override
    protected void initDynamic() {
        name = "Speed";
    }

    @Override
    protected void setConcreteValues() {
        statMap.put(Stat.ACCEL, 5.0 * itemSize.getStatMultiplier());
        dropRate = 0.5f * itemSize.getDropRate();

    }
}
