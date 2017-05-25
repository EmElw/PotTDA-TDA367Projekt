package com.pottda.game.application;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.pottda.game.model.Constants;
import com.pottda.game.model.builders.AbstractModelBuilder;

import static com.pottda.game.application.GameState.INVENTORY;
import static com.pottda.game.application.GameState.MAIN_CHOOSE;
import static com.pottda.game.application.GameState.MAIN_CONTROLS;
import static com.pottda.game.application.GameState.MAIN_MENU;
import static com.pottda.game.application.GameState.OPTIONS;
import static com.pottda.game.application.GameState.PAUSED;
import static com.pottda.game.application.GameState.RESTARTING;
import static com.pottda.game.application.GameState.WAITING_FOR_INVENTORY;
import static com.pottda.game.application.GameState.gameState;
import static com.pottda.game.model.Constants.HEIGHT;

public class PoTDAGame extends Game {
    private OrthographicCamera camera;

    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    private PausedScreen pausedScreen;
    private InventoryScreen inventoryScreen;

    @Override
    public void create() {
        Gdx.graphics.setTitle(Constants.GAME_TITLE);
        camera = new OrthographicCamera();
        gameScreen = new GameScreen();
        pausedScreen = new PausedScreen(gameScreen);
        menuScreen = new MenuScreen(gameScreen, pausedScreen);
        inventoryScreen = new InventoryScreen();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, HEIGHT * width / (float) height, HEIGHT);
        gameScreen.resize(width, height);
        menuScreen.resize(width, height);
        pausedScreen.resize(width, height);
        inventoryScreen.resize(width, height);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        if (gameState == MAIN_MENU || gameState == MAIN_CONTROLS || gameState == MAIN_CHOOSE) {
            menuScreen.render();
        } else if (gameState == RESTARTING) {
            doOnRestartGame();
        } else if (gameState == PAUSED || gameState == OPTIONS) {
            pausedScreen.render();
        } else if (gameState == INVENTORY) {
            inventoryScreen.render();
        } else {
            gameScreen.render();
        }
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
    public void dispose() {
        menuScreen.dispose();
        gameScreen.dispose();
        pausedScreen.dispose();
    }
}
