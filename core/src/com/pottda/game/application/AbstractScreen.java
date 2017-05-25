package com.pottda.game.application;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public abstract class AbstractScreen implements Screen {

    private Game game;

    public AbstractScreen(Game game){
        this.game = game;
    }

    protected void switchScreen(Screen screen){
        game.setScreen(screen);
    }

}
