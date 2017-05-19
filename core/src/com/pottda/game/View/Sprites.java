package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;


public enum Sprites {
    PLAYER("circletest.png"),
    ENEMY("circletestred.png"),
    PROJECTILE1("game/projectile1.png"),
    BORDER("game/border.png"),

    HEALTHBAR("hud/healthbar.png"),
    HEALTHBARRED("hud/health.png"),
    PAUSEBUTTON("hud/pauseButton.png"),

    CONTROLSTITLE("menu/chooseControls.png"),
    CONTROLSTOUCH("menu/touch.png"),
    CONTROLSKEYBOARDONLY("menu/keyOnly.png"),
    CONTROLSKEYBOARDMOUSE("menu/keyMouse.png"),
    MAINMENUBG("menu/bg.png"),

    CHOOSETITLE("menu/chooseDiff.png"),
    CHOOSEEASY("menu/easy.png"),
    CHOOSEHARD("menu/hard.png"),

    MAINMENUTITLE("menu/title.png"),
    MAINMENUSTART("menu/start.png"),
    MAINMENUQUIT("menu/quit.png"),

    OPTIONSMUSICBAR("hud/optionsVolumeBar.png"),
    OPTIONSMUSIC("hud/optionsVolume.png"),
    OPTIONSSFXBAR("hud/optionsVolumeBar.png"),
    OPTIONSSFX("hud/optionsVolume.png"),
    OPTIONSSFXTEXT("hud/text/sfx.png"),
    OPTIONSMUSICTEXT("hud/text/music.png"),
    OPTIONSRETURN("hud/optionsReturn.png"),
    OPTIONSBG("hud/pauseBackground.png"),

    PAUSERESUME("hud/pauseResume.png"),
    PAUSEOPTIONS("hud/pauseOptions.png"),
    PAUSEQUIT("hud/pauseQuit.png");

    public final Texture texture;

    Sprites(String internalFileName) {
        // Could do loading the first time the texture is requested if loading times get long
        Texture t;
        try {
             t = new Texture(Gdx.files.internal(internalFileName));
        } catch (NullPointerException e) {
            t = null;
        }
        texture = t;
    }
}
