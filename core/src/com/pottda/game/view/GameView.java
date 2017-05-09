package com.pottda.game.view;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by Rikard Teodorsson on 2017-05-08.
 */

public class GameView {
    private final Stage joystickStage;
    private final Stage gameStage;

    public GameView(Stage gameStage, Stage joystickStage) {
        this.joystickStage = joystickStage;
        this.gameStage = gameStage;
    }

    public void render() {
        joystickStage.draw();
        gameStage.draw();
    }

    public void dispose() {
        joystickStage.dispose();
        gameStage.draw();
    }

}
