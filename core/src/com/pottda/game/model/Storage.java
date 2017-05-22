package com.pottda.game.model;

import java.util.*;

/**
 * Created by Mr Cornholio on 21/05/2017.
 */
public class Storage {
    /**
     * A map with a String key (for the name of the item to be added) and an ItemStorage
     */
    private Map<String, ItemStorage> storageMap;

    /**
     * A boolean to keep track if the storage has changed since the last time it was accessed (usually from InventoryView
     * when it's meant to draw the storage)
     */
    private boolean updated;

    /**
     * Create a new storage
     */
    public Storage() {
        storageMap = new HashMap<String, ItemStorage>();
        updated = false;
    }

    /**
     * Takes in an item to add to storage, if the item already exists it just adds it to the currently
     * existing ItemStorage.
     * If it doesn't exist, it creates a new ItemStorage for that item.
     * Either way, it sets the status of storage to "updated"
     *
     * @param item
     */
    public void addItem(Item item) {
        if(storageMap.containsKey(item.name)) {
            storageMap.get(item.name).items.add(item);
        } else {
            storageMap.put(item.name, new ItemStorage(item));
        }
        updated = true;
    }

    /**
     *
     * @param itemName
     * @return return the amount of items with the same name as itemName that is currently in storage
     */
    public int getNrOf(String itemName) {
        return storageMap.get(itemName).items.size();
    }

    public Map<String, ItemStorage> getMap() {
        return storageMap;
    }

    /**
     * Gets an item from storage, does not remove items, use popItem for that
     *
     * @param name the name of the item to get
     * @return an item with the same name as the param
     * @throws Exception
     */
    public Item getItem(String name) throws Exception{
        ItemStorage storage;
        if((storage = storageMap.get(name)) != null) {
            return storage.items.get(0);
        }
        throw new Exception("Tried to get an item from an empty storage");
    }

    /**
     * Pops, aka retrieves, an item from storage (used in cases when moving items from say storage to inventory)
     *
     * @param name the name of the item to retrieve
     * @return an item with the same name as the param
     * @throws Exception
     */
    public Item popItem(String name) throws Exception{
        ItemStorage storage;
        if((storage = storageMap.get(name)) != null) {
            Item i = storage.items.get(0);
            if (storage.items.size() == 0){
                storageMap.remove(name);
            }
            return i;
        }
        throw new Exception("Tried to retrieve an item from an empty storage");
    }

    /**
     * Checks if the storage has been updated since last time
     * @return a boolean saying if the storage has been updated
     */
    public boolean isUpdate() {
        return updated;
    }

    public void setUpdate(boolean updated) {
        this.updated = updated;
    }

    /**
     * ItemStorage holds one specific item and keeps additional statistics of it
     */
    public class ItemStorage {
        /**
         * A list to store items of a certain name
         */
        private List<Item> items = new ArrayList<Item>();

        /**
         * Boolean used to check if the item is a new addition to storage
         */
        private boolean isNew;

        /**
         * String to check what kind of item it is (its name)
         */
        private String type;

        /**
         * Constructor, creates an ItemStorage from an item
         * @param item
         */
        public ItemStorage(Item item) {
            items.add(item);
            type = item.name;
            isNew = true;
        }

        /**
         * After a new item has been seen by the user, make it so that it doesn't stand out again.
         * @param isNew
         */
        // TODO change the isNew variable after exiting the Inventory in-game
        public void setNew(boolean isNew) {
            this.isNew = isNew;
        }

        /**
         * Gets, and removes an item from the item list
         * @return
         */
        private Item pop() {
            Item stuff = items.get(0);
            items.remove(0);
            return stuff;
        }
    }
}
