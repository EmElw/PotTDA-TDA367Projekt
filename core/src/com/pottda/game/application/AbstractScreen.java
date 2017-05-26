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
public abstract class AbstractScreen implements Screen {

    protected Game game;

    protected Stage stage;

    protected Camera camera;

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

    public void render(SpriteBatch batch, float delta) {

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        stage.act(delta);
        stage.draw();
        batch.end();
    }

    ;

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
