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
        basePositions = new HashSet<Point2i>();
        baseOutputs = new HashSet<Point2i>();
        statMap = new EnumMap<Stat, Double>(Stat.class);
        outputItems = new ArrayList<Item>();

        changedPositions = true;
        changedOutputs = true;

        rotatedOutputs = new HashSet<Point2i>();
        rotatedPositions = new HashSet<Point2i>();

        // Set default properties
        isPrimaryAttack = false;
        isProjectileModifier = false;
        isSecondaryAttack = false;
        dropRate = 0;
        itemSize = ItemSize.MINOR;
    }

    public void setSize(ItemSize itemSize) {
        basePositions.clear();
        this.itemSize = itemSize;
        setBasePositions();
        if (itemSize != ItemSize.NORMAL) {
            this.name = itemSize.getName() + name;
        }
    }

    protected abstract void setBasePositions();
}
