package com.pottda.game.model.items;

import com.pottda.game.model.Item;

import javax.vecmath.Point2i;

public abstract class SupportItem extends SizedItem {
    /**
     * Size for minor:
     * [X]
     *
     * Size for normal:
     * [X]X
     *
     * Size for big:
     *  X X
     * [X]X
     */

    @Override
    protected void setBasePositions(){
        switch (itemSize){
            case BIG:
                basePositions.add(new Point2i(1, 1));
                basePositions.add(new Point2i(0, 1));
            case NORMAL:
                basePositions.add(new Point2i(1, 0));
            case MINOR:
                basePositions.add(new Point2i(0, 0));
        }
    }

    @Override
    protected Item getNext(){
        return null;
    }
}
