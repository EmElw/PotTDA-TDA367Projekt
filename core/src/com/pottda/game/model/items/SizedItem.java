package com.pottda.game.model.items;

import com.pottda.game.model.Item;
import com.pottda.game.model.Stat;

import javax.vecmath.Point2i;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;

public abstract class SizedItem extends Item {
    ItemSize itemSize;

    @Override
    protected void init() {
        itemSize = ItemSize.MINOR;
        super.init();
    }

    public void setSize(ItemSize itemSize) {
        basePositions.clear();
        this.itemSize = itemSize;
        setBasePositions();
        if (itemSize != ItemSize.NORMAL) {
            this.name = itemSize.getName() + name;
        }
        setConcreteValues();
    }

    /**
     * Set the values of this item that relies on itemSize
     */
    protected abstract void setConcreteValues();

    protected abstract void setBasePositions();
}
