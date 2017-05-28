package com.pottda.game.model.items;

import com.pottda.game.model.ProjectileListener;

import javax.vecmath.Point2i;

public abstract class GenericProjectileModifier extends SizedItem {
    /**
     * Size for minot:
     * [X]O
     *
     * Size for normal:
     * X[X]O
     *
     * Size for big:
     * X
     * X[X]O
     * X
     */

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
