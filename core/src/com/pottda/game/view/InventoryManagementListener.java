package com.pottda.game.view;

import com.pottda.game.model.Item;

/**
 * Listens to input events in the inventory management screen
 */
public interface InventoryManagementListener {

    /**
     * Called when an {@link Item} in the storage list is touched
     *
     * @param itemName
     */
    void storageItemTouched(String itemName);

    /**
     * Called when the specific {@link Item} in the inventory is touched
     *
     * @param item
     */
    void inventoryItemTouched(Item item);

    /**
     * Called on touchUp when an item is dropped into a relevant coordinate in the inventory
     *
     * @param itemName
     * @param x
     * @param y
     */
    void storageItemDropped(String itemName, int x, int y);
}
