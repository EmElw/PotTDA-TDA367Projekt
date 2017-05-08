package com.pottda.game.view;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by Rikard Teodorsson on 2017-05-08.
 */

public class GameView {
    private final Stage stage;

    public GameView(Stage stage) {
        this.stage = stage;
    }

    public void render() {
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }

}
