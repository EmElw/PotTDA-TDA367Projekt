package com.pottda.game.application;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import static com.pottda.game.model.Constants.HEIGHT_VIEWPORT;
import static com.pottda.game.model.Constants.WIDTH_VIEWPORT;

public abstract class AbstractMenuScreen extends AbstractScreen {

    Camera camera;
    Stage stage;

    AbstractMenuScreen(Game game) {
        super(game);
        create();
    }

    void create(){
        camera = new OrthographicCamera();
        ((OrthographicCamera) camera).setToOrtho(false, WIDTH_VIEWPORT, HEIGHT_VIEWPORT);
    }

    @Override
    public final void render(SpriteBatch batch, float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (camera == null) {
            throw new Error("Uninstantiated camera");
        }
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        stage.act(delta);
        stage.draw();
        batch.end();
    }

    @Override
    public final void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, false);
        }
    }

    @Override
    public final void dispose() {
        stage.dispose();
    }
}
