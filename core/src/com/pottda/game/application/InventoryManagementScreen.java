package com.pottda.game.application;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.pottda.game.controller.view.InventoryManagementController;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pottda.game.model.Inventory;
import com.pottda.game.model.Storage;
import com.pottda.game.model.Storage;
import com.pottda.game.view.InventoryManagementView;

import static com.pottda.game.model.Constants.HEIGHT_VIEWPORT;
import static com.pottda.game.model.Constants.WIDTH_VIEWPORT;

import com.pottda.game.model.Storage;

public class InventoryManagementScreen extends AbstractMenuScreen {

    Inventory inventory;
    Storage storage;

    private InventoryManagementController controller;

    InventoryManagementScreen(Game game, Inventory inventory, Storage storage) {
        super(game);
        this.inventory = inventory;
        this.storage = storage;
        create(inventory, storage);
    }

    private void create(Inventory inventory, Storage storage) {

        stage = new Stage(new StretchViewport(WIDTH_VIEWPORT, HEIGHT_VIEWPORT));

        InventoryManagementView inventoryView = new InventoryManagementView(stage);
        inventoryView.updateStorageTable(storage);
        inventoryView.updateInventoryGroup(inventory);

        controller = new InventoryManagementController(inventory, storage, inventoryView);
    }
}
