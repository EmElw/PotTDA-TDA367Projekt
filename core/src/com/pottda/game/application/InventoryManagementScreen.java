package com.pottda.game.application;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.pottda.game.controller.view.InventoryManagementController;
import com.pottda.game.model.Inventory;
import com.pottda.game.model.Storage;
import com.pottda.game.view.InventoryManagementView;

import static com.pottda.game.assets.Constants.HEIGHT_VIEWPORT;
import static com.pottda.game.assets.Constants.SKIN_QH;
import static com.pottda.game.assets.Constants.WIDTH_VIEWPORT;

class InventoryManagementScreen extends AbstractMenuScreen {

    private final Screen parentScreen;

    InventoryManagementScreen(Game game, Screen parentScreen, Inventory inventory, Storage storage) {
        super(game);
        this.parentScreen = parentScreen;
        create(inventory, storage);
    }

    private void create(Inventory inventory, final Storage storage) {

        stage = new Stage(new StretchViewport(WIDTH_VIEWPORT, HEIGHT_VIEWPORT));


        InventoryManagementView inventoryView = new InventoryManagementView(stage);
        inventoryView.updateStorageTable(storage);
        inventoryView.updateInventoryGroup(inventory);

        new InventoryManagementController(inventory, storage, inventoryView);

        TextButton resumeButton = new TextButton("Continue", SKIN_QH);
        resumeButton.setPosition(stage.getWidth() - resumeButton.getWidth(), 0);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent evt, float x, float y) {
                switchScreen(parentScreen);
                storage.unflagNewItems();
                dispose();
            }
        });
        stage.addActor(resumeButton);
    }
}
