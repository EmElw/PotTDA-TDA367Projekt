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

import static com.pottda.game.application.Constants.PADDING;
import static com.pottda.game.model.Constants.HEIGHT_VIEWPORT;
import static com.pottda.game.model.Constants.WIDTH_VIEWPORT;

class PausedScreen extends AbstractScreen {

    private Screen gameScreen;

    // TODO access in nicer way
    private Skin skin = new Skin(Gdx.files.internal("skin/quantum-horizon-ui.json"));

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
        dispose();
    }


    private void setUpUI() {
        Table superTable = new Table();
        superTable.setFillParent(true);

        {
            Label sfxLabel = new Label("SFX", skin);
            superTable.add(sfxLabel).right().uniformX();

            sfxSlider = new Slider(0, 100, 1, false, skin);
            superTable.add(sfxSlider).left().expandX().row();

            Label musicLabel = new Label("MUSIC", skin);
            superTable.add(sfxLabel).right().uniformX();

            musicSlider = new Slider(0, 100, 1, false, skin);
            superTable.add(sfxSlider).left().expandX().row();

            resumeButton = new TextButton("RESUME", skin);
            superTable.add(resumeButton).bottom().left();

            toMenuButton = new TextButton("MAIN MENU", skin);
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
