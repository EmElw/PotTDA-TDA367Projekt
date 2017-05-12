package com.pottda.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;


public enum Sprites {
    PLAYER("circletest.png"),
    ENEMY("circletestred.png"),
    PROJECTLE1("game/projectile1.png");

    public final Texture texture;

    Sprites(String internalFileName) {
        // Could do loading the first time the texture is requested if loading times get long
        this.texture = new Texture(Gdx.files.internal(internalFileName));
    }
}
