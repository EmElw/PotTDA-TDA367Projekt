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
        // center camera to player each frame
        gameStage.getCamera().position.set(gameStage.getActors().get(0).getX(), gameStage.getActors().get(0).getY(), 0);

        // Update camera position before drawing stage to prevent shaking
        gameStage.draw();
        joystickStage.draw();
    }

    public void dispose() {
        joystickStage.dispose();
        gameStage.draw();
    }

}
