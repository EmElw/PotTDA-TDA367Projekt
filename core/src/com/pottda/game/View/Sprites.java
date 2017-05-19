package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;


public enum Sprites {
    PLAYER("circletest.png"),
    ENEMY("circletestred.png"),
    PROJECTILE1("game/projectile1.png"),
    BORDER("game/border.png"),

    MAINMENUBG("menu/bg.png"),

    TITLE("gameover/title.png"),
    RESTARTBUTTON("gameover/restart.png"),
    QUITBUTTON("gameover/quit.png");

    public final Texture texture;

    Sprites(String internalFileName) {
        // Could do loading the first time the texture is requested if loading times get long
        Texture t;
        try {
             t = new Texture(Gdx.files.internal(internalFileName));
        } catch (NullPointerException e) {
            t = null;
        }
        texture = t;
    }
}
