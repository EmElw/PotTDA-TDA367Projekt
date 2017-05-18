package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Rikard Teodorsson on 2017-05-18.
 */

public class PausedView {
    private static final String pauseBackgroundString = "hud/pauseBackground.png";
    private static final String pauseResumeString = "hud/pauseResume.png";
    private static final String pauseOptionsString = "hud/pauseOptions.png";
    private static final String pauseQuitString = "hud/pauseQuit.png";

    private Texture texture;
    private final Stage stage;

    private Image pauseBackground;
    private Image pauseResume;
    private Image pauseOptions;
    private Image pauseQuit;

    public PausedView(final Stage stage) {
        this.stage = stage;
        create();
    }

    private void create() {
        // Add pause background
        texture = new Texture(Gdx.files.internal(pauseBackgroundString));
        pauseBackground = new Image(texture);
        pauseBackground.setX(0);
        pauseBackground.setY(0);
        pauseBackground.setWidth(stage.getWidth());
        pauseBackground.setHeight(stage.getHeight());
        pauseBackground.setVisible(false);
        stage.addActor(pauseBackground);

        // Add pause resume button
        texture = new Texture(Gdx.files.internal(pauseResumeString)); // 300x120px
        pauseResume = new Image(texture);
        pauseResume.setX(stage.getWidth() / 2 - texture.getWidth() / 2);
        pauseResume.setY(stage.getHeight() - 150);
        pauseResume.setVisible(false);
        stage.addActor(pauseResume);

        // Add pause options button
        texture = new Texture(Gdx.files.internal(pauseOptionsString));
        pauseOptions = new Image(texture);
        pauseOptions.setX(stage.getWidth() / 2 - texture.getWidth() / 2);
        pauseOptions.setY(stage.getHeight() - 300);
        pauseOptions.setVisible(false);
        stage.addActor(pauseOptions);

        // Add pause quit button
        texture = new Texture(Gdx.files.internal(pauseQuitString));
        pauseQuit = new Image(texture);
        pauseQuit.setX(stage.getWidth() / 2 - texture.getWidth() / 2);
        pauseQuit.setY(30);
        pauseQuit.setVisible(false);
        stage.addActor(pauseQuit);
    }

}
