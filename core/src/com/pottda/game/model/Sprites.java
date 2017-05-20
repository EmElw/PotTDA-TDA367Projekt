package com.pottda.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;


public enum Sprites {
    PLAYER("circletest.png"),
    ENEMY("circletestred.png"),
    PROJECTILE1("game/projectile1.png"),
    BORDER("game/border.png"),
    QUITBUTTON("gameover/quit.png"),
    MAINMENUBG("menu/bg.png"),
    TITLE("menu/title.png"),
    RESTARTBUTTON("gameover/restart.png");

    public final String fileName;

    Sprites(String fileName) {
        this.fileName = fileName;
    }
}
