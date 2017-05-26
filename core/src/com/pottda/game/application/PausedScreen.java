package com.pottda.game.application;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import static com.pottda.game.application.Constants.DIVIDER_HEIGHT;
import static com.pottda.game.application.Constants.PADDING;
import static com.pottda.game.application.Constants.SKIN_QH;
import static com.pottda.game.model.Constants.HEIGHT_VIEWPORT;
import static com.pottda.game.model.Constants.WIDTH_VIEWPORT;

class PausedScreen extends AbstractScreen {

    private Screen gameScreen;


    private Slider sfxSlider;
    private Slider musicSlider;
    private TextButton resumeButton;
    private TextButton toMenuButton;

    PausedScreen(Game game, GameScreen gameScreen) {
        super(game);
        this.gameScreen = gameScreen;
        create();
    }

    private void create() {
        camera = new OrthographicCamera();
        ((OrthographicCamera) camera).setToOrtho(false, WIDTH_VIEWPORT, HEIGHT_VIEWPORT);

        stage = new Stage(new StretchViewport(WIDTH_VIEWPORT, HEIGHT_VIEWPORT));
        stage.setDebugAll(true);
        Gdx.input.setInputProcessor(stage);

        setUpUI();

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resumeGame();
            }
        });
        toMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toMainMenu();
            }
        });

        sfxSlider.addListener(new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                // setSfxVolume(x / getWidth); // TODO set volume
            }
        });
        musicSlider.addListener(new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                // setMusicVolume(x / getWidth); // TODO set volume
            }
        });
    }

    private void toMainMenu() {
        // TODO decide about this
        switchScreen(new MenuScreen(game));
        dispose();
    }

    private void resumeGame() {
        switchScreen(gameScreen);
        GameState.gameState = GameState.RUNNING;
        dispose();
    }

    private void setUpUI() {
        Table superTable = new Table();
        superTable.setFillParent(true);

        {
            Label pauseTitle = new Label("PAUSED", SKIN_QH, "title");
            superTable.add(pauseTitle).center().fillX().expandX().height(120).row();

            Label sfxVol = new Label("SFX", SKIN_QH);
            superTable.add(sfxVol).right().uniformX();

            sfxSlider = new Slider(0, 100, 1, false, SKIN_QH);
//                sfxSlider.setValue() // TODO set music with sliders
            superTable.add(sfxSlider).left().expandX().fillX();
            superTable.add().row();

            Label musicVol = new Label("Music", SKIN_QH);
            superTable.add(musicVol).right();

            musicSlider = new Slider(0, 100, 1, false, SKIN_QH);
            superTable.add(musicSlider).left().expandX().fillX();
            superTable.add().row();

            // Add a divider
            superTable.add().height(DIVIDER_HEIGHT).row();

            resumeButton = new TextButton("RESUME", SKIN_QH);
            superTable.add(resumeButton).bottom().left();

            toMenuButton = new TextButton("MAIN MENU", SKIN_QH);
            superTable.add(toMenuButton).bottom().right();
        }
        superTable.pad(PADDING);
        stage.addActor(superTable);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }


    @Override
    public void dispose() {
        stage.dispose();
    }
}
