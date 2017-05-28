package com.pottda.game.controller.view;

import com.pottda.game.model.*;
import com.pottda.game.view.InventoryManagementListener;
import com.pottda.game.view.InventoryManagementView;

/**
 * Class that acts as a controller and bridge between
 * an {@link com.pottda.game.model.Inventory} and a
 * {@link com.pottda.game.model.Storage}.
 */
public class InventoryManagementController implements InventoryChangeListener, StorageChangeListener, InventoryManagementListener {

    // Model data
    private final Inventory inventory;
    private final Storage storage;

    // View data
    private final InventoryManagementView view;

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
    public void storageItemTouched() {
    }

    @Override
    public void inventoryItemTouched() {

    }

    @Override
    public void storageItemToInventory(String itemName, int x, int y, int orientation) {
        Item item = storage.popItem(itemName);
        item.setX(x);
        item.setY(y);
        item.setOrientation(orientation);

        inventory.addItem(item);
    }

    @Override
    public void inventoryItemMoved(Item item, int x, int y, int orientation) {
        //inventory.removeItem(item);

        inventory.moveItem(item, x, y, orientation);
    }

    @Override
    public void inventoryItemToStorage(Item item) {
        inventory.removeItem(item);
        storage.addItem(item);
    }
}
