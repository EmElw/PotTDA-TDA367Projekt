package com.pottda.game.controller.view;

import com.pottda.game.model.Inventory;
import com.pottda.game.model.InventoryChangeListener;
import com.pottda.game.model.Storage;
import com.pottda.game.model.StorageChangeListener;
import com.pottda.game.view.InventoryView;

/**
 * Class that acts as a controller and bridge between
 * an {@link com.pottda.game.model.Inventory} and a
 * {@link com.pottda.game.model.Storage}.
 */
public class InventoryManagementController implements InventoryChangeListener, StorageChangeListener {

    private Inventory inventory;
    private Storage storage;

    private InventoryView view;

    public InventoryManagementController(Inventory inventory, Storage storage) {
        this.inventory = inventory;
        this.inventory.addInventoryChangeListener(this);

        this.storage = storage;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void inventoryChanged() {
        view.parseInventory(inventory);
    }

    @Override
    public void storageChanged() {
        view.parseStorage(storage);
    }
}
