package com.pottda.game.application;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.pottda.game.controller.ControllerOptions;

import static com.pottda.game.application.GameState.MAIN_MENU;
import static com.pottda.game.application.GameState.RUNNING;
import static com.pottda.game.application.GameState.gameState;
import static com.pottda.game.model.Constants.*;

class MenuScreen extends AbstractScreen {
    private final static int PADDING = 40;
    private final static Color bgColor = new Color(0xDACC09FF);

    private Stage stage;
    // TODO access in nicer way
    private Skin skin = new Skin(Gdx.files.internal("skin/quantum-horizon-ui.json"));

    private final GameScreen gameScreen;

    private TextButton startButton;
    private TextButton settingsButton;
    private TextButton quitButton;

    private Table settingsTable;
    private ButtonGroup<TextButton> controlButtons;

    MenuScreen(Game game, GameScreen gameScreen) {
        super(game);
        this.gameScreen = gameScreen;
        create();
    }

    private void create() {
        stage = new Stage(new StretchViewport(WIDTH_VIEWPORT, HEIGHT_VIEWPORT));
        stage.setDebugAll(true);
        Gdx.input.setInputProcessor(stage);

        setUpUI();
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent evt, float x, float y) {
                startGame();
            }
        });

        settingsTable.setVisible(false);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent evt, float x, float y) {
                settingsTable.setVisible(!settingsTable.isVisible());
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent evt, float x, float y) {
                quitGame();
            }
        });


        gameState = MAIN_MENU;


    }

    private void quitGame() {
        Gdx.app.exit();
    }

    private void startGame() {
        gameScreen.doOnStartGame();
        gameState = RUNNING;
        Gdx.input.setInputProcessor(ControllerOptions.joystickStage);   // TODO clean
        switchScreen(gameScreen);
    }

    private void setUpUI() {

        Table superTable = new Table();
        superTable.setFillParent(true);
        superTable.pad(PADDING);
        {
            Table buttonTable = new Table();
            {
                startButton = new TextButton("START", skin);
                buttonTable.add(startButton).fillX().row();

                settingsButton = new TextButton("SETTINGS", skin);
                buttonTable.add(settingsButton).fillX().row();

                quitButton = new TextButton("QUIT", skin);
                buttonTable.add(quitButton).fillX().row();
            }
            superTable.add(buttonTable).bottom().left().fillX().expand();

            settingsTable = new Table();
            {
                settingsTable.setBackground(solidBackground(bgColor));

                Label sfxVol = new Label("SFX", skin);
                settingsTable.add(sfxVol).right().uniformX();

                Slider sfxSlider = new Slider(0, 100, 1, false, skin);
                settingsTable.add(sfxSlider).left().expandX().fillX().row();

                Label musicVol = new Label("Music", skin);
                settingsTable.add(musicVol).right();

                Slider musicSlider = new Slider(0, 100, 1, false, skin);
                settingsTable.add(musicSlider).left().expandX().fillX().row();

                TextButton kmButton = new TextButton("Keyboard + Mouse", skin);
                settingsTable.add();
                settingsTable.add(kmButton).fillX().row();

                TextButton koButton = new TextButton("Keyboard only", skin);
                settingsTable.add();
                settingsTable.add(koButton).fillX().row();

                TextButton tchButton = new TextButton("Touch", skin);
                settingsTable.add();
                settingsTable.add(tchButton).fillX().row();

                controlButtons = new ButtonGroup<TextButton>(kmButton, koButton, tchButton);
                controlButtons.setChecked("Keyboard + Mouse");

                settingsTable.invalidateHierarchy();
            }
            superTable.add(settingsTable).bottom().right().fillX().expand();
        }
        stage.addActor(superTable);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
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

    @Override
    public void dispose() {
        stage.dispose();
    }

    private Drawable solidBackground(Color color) {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGB888);
        pm.setColor(color);
        pm.fill();
        Texture tex = new Texture(pm);
        pm.dispose();
        return new TextureRegionDrawable(new TextureRegion(tex));
    }


}

// Box2D.init();