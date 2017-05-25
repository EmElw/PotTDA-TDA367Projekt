package com.pottda.game.application;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.pottda.game.view.OptionsView;
import com.pottda.game.view.PausedView;
import com.pottda.game.view.SoundsAndMusic;

import static com.pottda.game.application.GameState.OPTIONS;
import static com.pottda.game.application.GameState.PAUSED;
import static com.pottda.game.application.GameState.RUNNING;
import static com.pottda.game.application.GameState.gameState;
import static com.pottda.game.model.Constants.HEIGHT_VIEWPORT;
import static com.pottda.game.model.Constants.WIDTH_VIEWPORT;

class PausedScreen extends AbstractScreen {
    private Stage pausedStage;
    private Stage optionsStage;

    private PausedView pausedView;
    private OptionsView optionsView;

    private final SoundsAndMusic soundsAndMusic;
    private Screen gameScreen;

    PausedScreen(Game game, GameScreen gameScreen) {
        super(game);
        this.gameScreen = gameScreen;
        soundsAndMusic = gameScreen.getSoundsAndMusic();
        create();
    }

    private void create() {
        pausedStage = new Stage(new StretchViewport(WIDTH_VIEWPORT, HEIGHT_VIEWPORT));
        optionsStage = new Stage(new StretchViewport(WIDTH_VIEWPORT, HEIGHT_VIEWPORT));
        pausedView = new PausedView(pausedStage);
        optionsView = new OptionsView(optionsStage);
    }

    @Override
    public void resize(int width, int height) {
        optionsStage.getViewport().update(width, height, false);
        pausedStage.getViewport().update(width, height, false);
    }


    @Override
    public void render(SpriteBatch batch, float delta) {
        switch (gameState) {
            case PAUSED:
                // Draw the pause menu
                pausedView.render();
                break;
            case OPTIONS:
                // Draw the options menu
                optionsView.render();
                break;
        }

        checkTouch();

    }

    @Override
    public void dispose() {
        pausedStage.dispose();
        optionsStage.dispose();
    }

    private void checkTouch() { // TODO move to a controller class
        if (Gdx.input.justTouched()) { // Only check first touch
            // Get hudStage camera and unproject to get correct coordinates!
            Vector3 vector3 = pausedStage.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            switch (gameState) {
                case PAUSED:
                    if (pausedView.checkIfTouchingPauseResume(vector3)) {
                        // Touching pause resume
                        switchScreen(gameScreen);
                        gameState = RUNNING;
                    } else if (pausedView.checkIfTouchingPauseOptions(vector3)) {
                        // Touching pause options
                        gameState = OPTIONS;
                    } else if (pausedView.checkIfTouchingPauseQuit(vector3)) {
                        // Touching pause quit
                        Gdx.app.exit();
                    }
                    break;
                case OPTIONS:
                    if (optionsView.checkIfTouchingOptionsReturn(vector3)) {
                        // Touched
                        gameState = PAUSED;
                    } else if (optionsView.checkIfTouchingOptionsMusic(vector3)) {
                        soundsAndMusic.setMusicVolume(optionsView.getNewMusicVolume(vector3));
                    } else if (optionsView.checkIfTouchingOptionsSFX(vector3)) {
                        soundsAndMusic.setSFXVolume(optionsView.getNewSFXVolume(vector3));
                    }
                    break;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            switch (gameState) {
                case PAUSED:
                    gameState = RUNNING;
                    break;
                case OPTIONS:
                    gameState = PAUSED;
                    break;
            }
        }
    }

}
