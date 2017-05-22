package com.pottda.game.model.items;

import com.pottda.game.model.Stat;

public class SpeedItem extends SupportItem {
    @Override
    protected void initDynamic() {
        statMap.put(Stat.HEALTH, 5.0 * itemSize.getStatMultiplier());
        dropRate = 0.5f * itemSize.getDropRate();
        name = itemSize.getName() + "Speed Module";
        setBasePositions();
    }
}