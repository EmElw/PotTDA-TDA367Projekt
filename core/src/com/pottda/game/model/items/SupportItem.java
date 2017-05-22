package com.pottda.game.model.items;

import com.pottda.game.model.Item;

import javax.vecmath.Point2i;

abstract class SupportItem extends Item {
    ItemSize itemSize = ItemSize.NORMAL;

    public void setSize(ItemSize itemSize){
        this.itemSize = itemSize;
    }

    void setBasePositions(){
        switch (itemSize){
            case BIG:
                baseOutputs.add(new Point2i(1, 1));
                baseOutputs.add(new Point2i(0, 1));
            case NORMAL:
                baseOutputs.add(new Point2i(1, 0));
            case MINOR:
                baseOutputs.add(new Point2i(0, 0));
        }
    }
}
