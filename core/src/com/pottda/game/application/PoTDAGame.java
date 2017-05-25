package com.pottda.game.application;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pottda.game.model.Constants;
import com.pottda.game.model.builders.AbstractModelBuilder;

import static com.pottda.game.application.GameState.INVENTORY;
import static com.pottda.game.application.GameState.MAIN_CHOOSE;
import static com.pottda.game.application.GameState.MAIN_CONTROLS;
import static com.pottda.game.application.GameState.MAIN_MENU;
import static com.pottda.game.application.GameState.OPTIONS;
import static com.pottda.game.application.GameState.PAUSED;
import static com.pottda.game.application.GameState.RESTARTING;
import static com.pottda.game.application.GameState.gameState;
import static com.pottda.game.model.Constants.HEIGHT_VIEWPORT;

public class PoTDAGame extends Game {

    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    private PausedScreen pausedScreen;

    private SpriteBatch batch;

    @Override
    public void create() {
        Gdx.graphics.setTitle(Constants.GAME_TITLE);

        batch = new SpriteBatch();

        setScreen(new MenuScreen(this, new GameScreen(this, null)));
    }

    /**
     * Restarts the game by recreating everything
     */
    private void doOnRestartGame() {
        dispose();
        AbstractModelBuilder.clear();
        create();
    }

    @Override
    public void render() {
        if (screen != null) {
            ((AbstractScreen) screen).render(batch, Gdx.graphics.getDeltaTime());
        }
    }


    @Override
    public void dispose() {
        menuScreen.dispose();
        gameScreen.dispose();
        pausedScreen.dispose();
    }
}
