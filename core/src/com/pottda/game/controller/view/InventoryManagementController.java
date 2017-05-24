package com.pottda.game.controller.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.pottda.game.model.*;
import com.pottda.game.view.AtlasCreator;
import com.pottda.game.view.InventoryManagementListener;
import com.pottda.game.view.InventoryManagementView;

/**
 * Class that acts as a controller and bridge between
 * an {@link com.pottda.game.model.Inventory} and a
 * {@link com.pottda.game.model.Storage}.
 */
public class InventoryManagementController implements InventoryChangeListener, StorageChangeListener, InventoryManagementListener {

    // Model data
    private Inventory inventory;
    private Storage storage;

    // View data
    private InventoryManagementView view;

    public InventoryManagementController(Inventory inventory, Storage storage, InventoryManagementView view) {
        this.inventory = inventory;
        this.view = view;
        this.storage = storage;

        this.inventory.addInventoryChangeListener(this);
        this.storage.addStorageChangeListener(this);
        this.view.addListener(this);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Storage getStorage() {
        return storage;
    }

    public InventoryManagementView getView() {
        return view;
    }

    @Override
    public void inventoryChanged() {
        view.updateInventoryGroup(inventory);
    }

    @Override
    public void storageChanged() {
        view.updateStorageTable(storage);
    }

    @Override
    public void storageItemTouched(String itemName) {
    }

    @Override
    public void inventoryItemTouched(Item item) {

    }

    @Override
    public void touchUp() {
    }
}
