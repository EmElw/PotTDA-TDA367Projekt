package com.pottda.game.application;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.pottda.game.application.Constants.SKIN_QH;
import static com.pottda.game.application.GameState.RUNNING;
import static com.pottda.game.application.GameState.gameState;
import static com.pottda.game.model.Constants.HEIGHT_VIEWPORT;
import static com.pottda.game.model.Constants.WIDTH_VIEWPORT;

/**
 * Created by Magnus on 2017-05-26.
 */
public class GameOverScreen extends AbstractScreen {
    private int score;

    private TextButton menuButton;
    private TextButton restartButton;

    public GameOverScreen(Game game, int score) {
        super(game);
        this.score = score;
        create();
    }

    private void create() {
        camera = new OrthographicCamera();
        ((OrthographicCamera) camera).setToOrtho(false, WIDTH_VIEWPORT, HEIGHT_VIEWPORT);

        stage = new GameOverStage(new StretchViewport(WIDTH_VIEWPORT, HEIGHT_VIEWPORT));
        stage.setDebugAll(true);
        Gdx.input.setInputProcessor(stage);

        restartButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                restartGame();
            }
        });

        menuButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                toMainMenu();
            }
        });
    }

    private void toMainMenu() {
        GameState.gameState = GameState.MAIN_MENU;
        switchScreen(new MenuScreen(game));
        dispose();
    }

    private void restartGame() {
        GameScreen gs = new GameScreen(game);
        gameState = RUNNING;
        switchScreen(gs);
        dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private class GameOverStage extends Stage {
        GameOverStage(Viewport viewport) {
            super(viewport);
            initActors();
        }

        private void initActors() {
            Table superTable = new Table();
            superTable.setFillParent(true);
            {
                Label title = new Label("Game Over", SKIN_QH, "title");
                superTable.add(title).center().height(125).row();

                restartButton = new TextButton("Restart", SKIN_QH);
                superTable.add(restartButton).bottom().left().uniformX();

                Label scoreLabel = new Label("Score: " + score, SKIN_QH);
                superTable.add(scoreLabel).center().row();

                menuButton = new TextButton("Main Menu", SKIN_QH);
                superTable.add(menuButton).top().left().uniformX();

                Table highscoreTable = new Table(SKIN_QH);  // TODO evaluate
                superTable.add(highscoreTable);

            }
            this.addActor(superTable);
        }
    }
}