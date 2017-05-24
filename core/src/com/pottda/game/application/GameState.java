package com.pottda.game.application;

enum GameState {
    NONE,
    RUNNING,
    PAUSED,
    OPTIONS,
    MAIN_MENU,
    MAIN_CHOOSE,
    MAIN_CONTROLS,
    WAITING_FOR_INVENTORY,
    GAME_OVER,
    RESTARTING;

    static GameState gameState;
}
