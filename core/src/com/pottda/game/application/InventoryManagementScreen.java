package com.pottda.game.application;

import com.badlogic.gdx.Game;
import com.pottda.game.model.Inventory;

public class InventoryManagementScreen extends AbstractScreen {


    Inventory inventory;
    Storage storage;

    InventoryManagementScreen(Game game, Inventory inventory, Storage storage) {
        super(game);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose() {
    }
}
