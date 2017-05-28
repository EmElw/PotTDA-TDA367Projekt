package com.pottda.game.application;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by Magnus on 2017-05-26.
 */
public class Constants {
    final static int PADDING = 40;
    final static Color bgColor = new Color(0xDACC09FF);
    final static int DIVIDER_HEIGHT = 70;
    // TODO access in nicer way
    public final static Skin SKIN_QH = new Skin(Gdx.files.internal("skin/quantum-horizon-ui.json"));
}
