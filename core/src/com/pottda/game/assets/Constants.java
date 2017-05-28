package com.pottda.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class Constants {
    public final static int PADDING = 40;
    public final static Color bgColor = new Color(0xDACC09FF);
    public final static int DIVIDER_HEIGHT = 70;
    // TODO access in nicer way
    public final static Skin SKIN_QH = new Skin(Gdx.files.internal("skin/quantum-horizon-ui.json"));
}
