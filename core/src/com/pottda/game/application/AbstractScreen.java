package com.pottda.game.application;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class AbstractScreen implements Screen {

    protected Game game;

    public AbstractScreen(Game game) {
        this.game = game;
    }

    protected void switchScreen(Screen screen) {
        game.setScreen(screen);
    }

    @Override
    public void show() {

    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public final void render(float delta) {
    }

    public abstract void render(SpriteBatch batch, float delta);

    @Override
    public void hide() {

    }

}
