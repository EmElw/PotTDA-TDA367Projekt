package com.pottda.game.view;

import com.pottda.game.model.Item;

/**
 * Listens to input events in the inventory management screen
 */
public interface InventoryManagementListener {

    /**
     * Called when an {@link Item} in the storage list is touched
     *
     * @param itemName a {@link String} with the external item name
     */
    void storageItemTouched(String itemName);

    /**
     * Called when the specific {@link Item} in the inventory is touched
     *
     * @param item an {@link Item}
     */
    void inventoryItemTouched(Item item);

    void touchUp();
}
