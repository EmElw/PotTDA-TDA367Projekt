package com.pottda.game.application;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pottda.game.model.Constants;

public class PoTDAGame extends Game {

    private SpriteBatch batch;

    @Override
    public void create() {
        Gdx.graphics.setTitle(Constants.GAME_TITLE);

        batch = new SpriteBatch();

        setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        if (screen != null) {
            ((AbstractScreen) screen).render(batch, Gdx.graphics.getDeltaTime());
        }
    }
}
