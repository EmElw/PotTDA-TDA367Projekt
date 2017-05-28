package com.pottda.game.model.items;

import com.pottda.game.model.Item;

public abstract class SizedItem extends Item {
    ItemSize itemSize;

    @Override
    protected void init() {
        itemSize = ItemSize.MINOR;
        super.init();
    }

    public void setSize(ItemSize itemSize) {
        this.itemSize = itemSize;
        if (itemSize != ItemSize.NORMAL) {
            this.name = itemSize.getName() + name;
        }
    }

    protected abstract void setBasePositions();
}
