package com.pottda.game.model.items;

import com.pottda.game.model.Item;
import com.pottda.game.model.Stat;

public class HealthItem extends SupportItem{
    @Override
    protected void initDynamic() {
        statMap.put(Stat.HEALTH, 50.0 * itemSize.getStatMultiplier());
        dropRate = 0.75f * itemSize.getDropRate();
        name = itemSize.getName() + "Health Module";
        setBasePositions();
    }
}
