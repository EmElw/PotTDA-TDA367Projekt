package com.pottda.game.controller.view;

import com.pottda.game.model.Inventory;
import com.pottda.game.model.InventoryChangeListener;
import com.pottda.game.model.Storage;
import com.pottda.game.model.StorageChangeListener;
import com.pottda.game.view.InventoryManagementListener;
import com.pottda.game.view.InventoryView;

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
    private InventoryView view;

    public InventoryManagementController(Inventory inventory, Storage storage, InventoryView view) {
        this.inventory = inventory;
        this.view = view;
        this.storage = storage;

        this.inventory.addInventoryChangeListener(this);
        this.storage.addStorageChangeListener(this);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Storage getStorage() {
        return storage;
    }

    public InventoryView getView() {
        return view;
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
