package com.pottda.game.application;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Abstraction to interact with Game in smoother ways
 */
abstract class AbstractScreen implements Screen {

    protected Game game;

    AbstractScreen(Game game) {
        this.game = game;
    }

    void switchScreen(Screen screen) {
        game.setScreen(screen);
    }

    @Override
    public final void render(float delta) {
        // Should not be used, use render(SpriteBatch, float) to draw more efficiently
    }

    public abstract void render(SpriteBatch batch, float delta);


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
    public void hide() {

    }

}
