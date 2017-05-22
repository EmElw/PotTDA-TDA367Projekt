package com.pottda.game.model;

public enum Sprites {
    PLAYER("circletest.png"),
    ENEMY("circletestred.png"),
    ENEMYPROJECTILE("game/projectile1.png"),
    PLAYERPROJECTILE("game/projectile2.png"),
    BORDER("game/border.png"),
    QUITBUTTON("gameover/quit.png"),
    MAINMENUBG("menu/bg.png"),
    TITLE("menu/title.png"),
    RESTARTBUTTON("gameover/restart.png"),

    HEALTHBAR("hud/healthbar.png"),
    HEALTHBARRED("hud/health.png"),
    PAUSEBUTTON("hud/pauseButton.png"),

    CONTROLSTITLE("menu/chooseControls.png"),
    CONTROLSTOUCH("menu/touch.png"),
    CONTROLSKEYBOARDONLY("menu/keyOnly.png"),
    CONTROLSKEYBOARDMOUSE("menu/keyMouse.png"),

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

    public final String fileName;

    Sprites(String fileName) {
        this.fileName = fileName;
    }

}
