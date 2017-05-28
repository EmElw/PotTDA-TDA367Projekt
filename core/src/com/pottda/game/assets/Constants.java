package com.pottda.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class Constants {
    public static final String GAME_TITLE = "Panic on TDAncefloor";

    private static final float SCALING = 2f;

    public static final float WIDTH_VIEWPORT = 800;
    public static final float HEIGHT_VIEWPORT = 480;
    public static final float WIDTH_METERS = 30 * SCALING;
    public static final float HEIGHT_METERS = 18 * SCALING;
    public static final float HEIGHT_RATIO = WIDTH_METERS / WIDTH_VIEWPORT / SCALING;
    public static final float WIDTH_RATIO = HEIGHT_METERS / HEIGHT_VIEWPORT / SCALING;

    public final static int PADDING = 40;
    public final static Color bgColor = new Color(0xDACC09FF);
    public final static int DIVIDER_HEIGHT = 70;
    // TODO access in nicer way
    public final static Skin SKIN_QH = new Skin(Gdx.files.internal("skin/quantum-horizon-ui.json"));
}
