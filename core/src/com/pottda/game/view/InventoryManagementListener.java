package com.pottda.game.view;

import com.pottda.game.model.Item;

/**
 * Listens to input events in the inventory management screen
 */
public interface InventoryManagementListener {

    /**
     * Called when an {@link Item} in the storage list is touched
     *
     */
    void storageItemTouched();

    /**
     * Called when the specific {@link Item} in the inventory is touched
     *
     */
    void inventoryItemTouched();

    /**
     * Called on acceptButton when an item is dropped into a relevant coordinate in the inventory
     *
     * @param itemName
     * @param x
     * @param y
     */
    void storageItemToInventory(String itemName, int x, int y, int orientation);

    /**
     * Called on acceptButton when an item is moved in the inventory
     * @param item
     */
    void inventoryItemMoved(Item item, int x, int y, int orientation);

    void inventoryItemToStorage(Item item);
}
