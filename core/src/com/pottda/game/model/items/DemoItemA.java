package com.pottda.game.model.items;

import com.pottda.game.model.AttackItem;

/**
 * Created by Magnus on 2017-05-05.
 */
public class DemoItemA extends AttackItem {

    /*
    Shaped like

         x x o
        [X]x
     */
    @Override
    public void initDynamic() {
        isAttackItem = true;
        basePositions.add(new int[]{0, 0});
        basePositions.add(new int[]{1, 0});
        basePositions.add(new int[]{0, 1});
        basePositions.add(new int[]{1, 1});

        baseOutputs.add(new int[]{2, 1});
    }

}
