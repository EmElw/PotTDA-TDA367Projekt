package com.pottda.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.pottda.game.view.LibGDXApp;

public class MyGame extends ApplicationAdapter {

    private LibGDXApp libGDXApp;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 480;

    @Override
    public void create() {
        libGDXApp = new LibGDXApp(WIDTH, HEIGHT);
        libGDXApp.create();
    }

    @Override
    public void render() {
        libGDXApp.render();
    }

    @Override
    public void dispose() {
        libGDXApp.dispose();
    }
}
