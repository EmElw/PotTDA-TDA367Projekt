package com.pottda.game.model.items;

import com.pottda.game.model.Item;

public abstract class SizedItem extends Item{
    ItemSize itemSize = ItemSize.MINOR;

    public void setSize(ItemSize itemSize){
        this.itemSize = itemSize;
    }

    protected abstract void setBasePositions();
}
