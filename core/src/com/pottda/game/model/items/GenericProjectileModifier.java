package com.pottda.game.model.items;

import com.pottda.game.model.Item;

import javax.vecmath.Point2i;

public abstract class GenericProjectileModifier extends SizedItem {

    @Override
    protected void setBasePositions() {
        switch (itemSize) {
            case BIG:
                basePositions.add(new Point2i(-1, -1));
                basePositions.add(new Point2i(-1, 1));
            case NORMAL:
                basePositions.add(new Point2i(-1, 0));
            case MINOR:
                basePositions.add(new Point2i(0, 0));
                baseOutputs.add(new Point2i(1, 0));
        }
    }
}
