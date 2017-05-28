package com.pottda.game.model;

public class Constants {

    public static final String GAME_TITLE = "Panic on TDAncefloor";

    private static final float SCALING = 2f;

    public static final float WIDTH_VIEWPORT = 800;
    public static final float HEIGHT_VIEWPORT = 480;
    public static final float WIDTH_METERS = 30 * SCALING;
    public static final float HEIGHT_METERS = 18 * SCALING;
    public static final float HEIGHT_RATIO = WIDTH_METERS / WIDTH_VIEWPORT / SCALING;
    public static final float WIDTH_RATIO = HEIGHT_METERS / HEIGHT_VIEWPORT / SCALING;

}
