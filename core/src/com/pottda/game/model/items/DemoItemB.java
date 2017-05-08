package com.pottda.game.model.items;

import com.pottda.game.model.AttackItem;
import com.pottda.game.model.ProjectileListener;

/**
 * Created by Magnus on 2017-05-05.
 */
public class DemoItemB extends AttackItem implements ProjectileListener {

    /*
    Shaped like

            x x o
        [X] x
     */
    @Override
    public void initDynamic() {

        basePositions.add(new int[]{0, 0});
        basePositions.add(new int[]{1, 0});
        basePositions.add(new int[]{1, 1});
        basePositions.add(new int[]{2, 1});

        baseOutputs.add(new int[]{3, 1});
    }
}
