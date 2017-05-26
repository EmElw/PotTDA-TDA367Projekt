package com.pottda.game.application;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pottda.game.controller.ControllerOptions;

import static com.pottda.game.application.Constants.SKIN_QH;
import static com.pottda.game.application.GameState.MAIN_MENU;
import static com.pottda.game.application.GameState.RUNNING;
import static com.pottda.game.application.GameState.gameState;
import static com.pottda.game.model.Constants.*;

class MenuScreen extends AbstractScreen {


    private TextButton startButton;
    private TextButton settingsButton;
    private TextButton quitButton;

    private Table settingsTable;

    private TextButton keyboardMouseControlsButton;
    private TextButton keyboardOnlyControlsButton;
    private TextButton touchControlsButton;

    private Slider musicSlider;
    private Slider sfxSlider;


    MenuScreen(Game game) {
        super(game);
        create();
    }

    private void create() {
        camera = new OrthographicCamera();
        ((OrthographicCamera) camera).setToOrtho(false, WIDTH_VIEWPORT, HEIGHT_VIEWPORT);

        stage = new MenuStage(new StretchViewport(WIDTH_VIEWPORT, HEIGHT_VIEWPORT));
        stage.setDebugAll(true);
        Gdx.input.setInputProcessor(stage);


        // Actor logic is here
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

        keyboardMouseControlsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent evt, float x, float y) {
                keyboardMouseControlsButton.setChecked(true);
                ControllerOptions.controllerSettings = ControllerOptions.ControllerMode.KEYBOARD_MOUSE;
            }
        });
        keyboardOnlyControlsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent evt, float x, float y) {
                keyboardOnlyControlsButton.setChecked(true);
                ControllerOptions.controllerSettings = ControllerOptions.ControllerMode.KEYBOARD_ONLY;
            }
        });
        touchControlsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent evt, float x, float y) {
                touchControlsButton.setChecked(true);
                ControllerOptions.controllerSettings = ControllerOptions.ControllerMode.TOUCH_JOYSTICK;
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


        gameState = MAIN_MENU;
    }

    private void quitGame() {
        Gdx.app.exit();
    }

    private void startGame() {
        GameScreen gs = new GameScreen(game);
        gameState = RUNNING;
        switchScreen(gs);
        dispose();
    }

    private void checkControlButton() {
        switch (ControllerOptions.controllerSettings) {
            case TOUCH_JOYSTICK:
                touchControlsButton.isChecked();
                break;
            case KEYBOARD_MOUSE:
                keyboardMouseControlsButton.isChecked();
                break;
            case KEYBOARD_ONLY:
                keyboardOnlyControlsButton.isChecked();
                break;
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
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

    private class MenuStage extends Stage {

        public MenuStage(Viewport viewport) {
            super(viewport);
            initActors();
        }

        private void initActors() {

            Table superTable = new Table();
            superTable.setFillParent(true);
            superTable.pad(Constants.PADDING);
            {
                Table buttonTable = new Table();
                {
                    startButton = new TextButton("START", SKIN_QH);
                    buttonTable.add(startButton).fillX().row();

                    settingsButton = new TextButton("SETTINGS", SKIN_QH);
                    buttonTable.add(settingsButton).fillX().row();

                    quitButton = new TextButton("QUIT", SKIN_QH);
                    buttonTable.add(quitButton).fillX().row();
                }
                superTable.add(buttonTable).bottom().left().fillX().expand();

                settingsTable = new Table();
                {
                    settingsTable.setBackground(solidBackground(Constants.bgColor));

                    Label sfxVol = new Label("SFX", SKIN_QH);
                    settingsTable.add(sfxVol).right().uniformX();

                    sfxSlider = new Slider(0, 100, 1, false, SKIN_QH);
//                sfxSlider.setValue() // TODO set music with sliders
                    settingsTable.add(sfxSlider).left().expandX().fillX().row();

                    Label musicVol = new Label("Music", SKIN_QH);
                    settingsTable.add(musicVol).right();

                    musicSlider = new Slider(0, 100, 1, false, SKIN_QH);
                    settingsTable.add(musicSlider).left().expandX().fillX().row();

                    keyboardMouseControlsButton = new TextButton("Keyboard + Mouse", SKIN_QH);
                    settingsTable.add();
                    settingsTable.add(keyboardMouseControlsButton).fillX().row();

                    keyboardOnlyControlsButton = new TextButton("Keyboard only", SKIN_QH);
                    settingsTable.add();
                    settingsTable.add(keyboardOnlyControlsButton).fillX().row();

                    touchControlsButton = new TextButton("Touch", SKIN_QH);
                    settingsTable.add();
                    settingsTable.add(touchControlsButton).fillX().row();

                    ButtonGroup<TextButton> controlButtons = new ButtonGroup<TextButton>(keyboardMouseControlsButton, keyboardOnlyControlsButton, touchControlsButton);
                    controlButtons.setChecked("Keyboard + Mouse");

                    checkControlButton();

                    settingsTable.invalidateHierarchy();
                }
                superTable.add(settingsTable).bottom().right().fillX().expand();
            }
            addActor(superTable);
        }
    }
}