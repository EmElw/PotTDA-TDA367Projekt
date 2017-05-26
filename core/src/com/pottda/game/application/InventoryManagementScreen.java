package com.pottda.game.application;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.pottda.game.controller.view.InventoryManagementController;
import com.pottda.game.model.Inventory;
import com.pottda.game.model.Storage;
import com.pottda.game.view.InventoryManagementView;

import static com.pottda.game.model.Constants.HEIGHT_VIEWPORT;
import static com.pottda.game.model.Constants.WIDTH_VIEWPORT;

public class InventoryManagementScreen extends AbstractScreen {


    private InventoryManagementController controller;

    InventoryManagementScreen(Game game, Inventory inventory, Storage storage) {
        super(game);
        create(inventory, storage);
    }

    private void create(Inventory inventory, Storage storage) {
        camera = new OrthographicCamera();
        ((OrthographicCamera) camera).setToOrtho(false, WIDTH_VIEWPORT, HEIGHT_VIEWPORT);

        stage = new Stage();

        InventoryManagementView inventoryView = new InventoryManagementView(stage);
        inventoryView.updateStorageTable(storage);
        inventoryView.updateInventoryGroup(inventory);

        controller = new InventoryManagementController(inventory, storage, inventoryView);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void dispose() {
    }
}
