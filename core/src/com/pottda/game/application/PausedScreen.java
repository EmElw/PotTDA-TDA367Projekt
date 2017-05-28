package com.pottda.game.application;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pottda.game.view.SoundsAndMusic;

import static com.pottda.game.assets.Constants.*;

class PausedScreen extends AbstractMenuScreen {

    private final Screen gameScreen;

    private Slider sfxSlider;
    private Slider musicSlider;
    private TextButton resumeButton;
    private TextButton toMenuButton;

    PausedScreen(Game game, GameScreen gameScreen) {
        super(game);
        this.gameScreen = gameScreen;
    }

    @Override
    void create() {
        super.create();

        stage = new PausedScreen.PausedStage(new StretchViewport(WIDTH_VIEWPORT, HEIGHT_VIEWPORT));
        stage.setDebugAll(false);
        Gdx.input.setInputProcessor(stage);

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
                final float newVolume = x / musicSlider.getWidth();
                SoundsAndMusic.setMusicVolume(newVolume);
            }
        });
        musicSlider.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                final float newVolume = x / musicSlider.getWidth();
                SoundsAndMusic.setMusicVolume(newVolume);
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

    private class PausedStage extends Stage {
        PausedStage(Viewport viewport) {
            super(viewport);
            initActor();
        }

        private void initActor() {
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
                musicSlider.setValue(SoundsAndMusic.getMusicVolume() * musicSlider.getMaxValue());
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
            this.addActor(superTable);
        }
    }
}
