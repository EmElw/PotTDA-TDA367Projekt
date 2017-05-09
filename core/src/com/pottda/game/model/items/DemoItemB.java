package com.pottda.game.model.items;

import com.pottda.game.model.AttackItem;

import javax.vecmath.Point2i;

/**
 * Created by Magnus on 2017-05-05.
 */
public class DemoItemB extends AttackItem {

    /*
    Shaped like

            x x o
        [X] x
     */
    @Override
    public void initDynamic() {
        isPrimaryAttack = true;

        basePositions.add(new Point2i(0, 0));
        basePositions.add(new Point2i(1, 0));
        basePositions.add(new Point2i(1, 1));
        basePositions.add(new Point2i(2, 1));

        baseOutputs.add(new Point2i(3, 1));
    }
}
