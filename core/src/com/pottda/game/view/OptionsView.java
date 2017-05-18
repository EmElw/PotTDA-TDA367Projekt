package com.pottda.game.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Rikard Teodorsson on 2017-05-18.
 */

public class OptionsView {
    private static final String optionsMusicBarString = "hud/optionsVolumeBar.png";
    private static final String optionsMusicString = "hud/optionsVolume.png";
    private static final String optionsSFXBarString = "hud/optionsVolumeBar.png";
    private static final String optionsSFXString = "hud/optionsVolume.png";
    private static final String optionsSFXTextString = "hud/text/sfx.png";
    private static final String optionsMusicTextString = "hud/text/music.png";

    private Texture texture;
    private final Stage stage;

    private Image optionsMusicBar;
    private Image optionsMusic;
    private Image optionsSFXBar;
    private Image optionsSFX;
    private Image optionsSFXText;
    private Image optionsMusicText;
    private Image optionReturn;

    public OptionsView(final Stage stage) {
        this.stage = stage;
        create();
    }

    private void create() {

    }

}
