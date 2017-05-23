package com.pottda.game.model;

import java.util.*;

/**
 * Created by Mr Cornholio on 21/05/2017.
 */
public class Storage {

    private List<StorageChangeListener> listeners;

    /**
     * A map with a String key (for the name of the item to be added) and an ItemStorage
     */
    private Map<String, ItemStorage> storageMap;


    /**
     * Create a new storage
     */
    public Storage() {
        storageMap = new HashMap<String, ItemStorage>();
        listeners = new ArrayList<StorageChangeListener>();
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
        if (storageMap.containsKey(item.getName())) {
            storageMap.get(item.getName()).items.add(item);
        } else {
            storageMap.put(item.getName(), new ItemStorage(item));
        }
        notifyListeners();
    }

    /**
     * Gets an item from storage, does not remove items, use popItem for that
     *
     * @param name the name of the item to get
     * @return an item with the same name as the param
     * @throws Exception
     */
    public Item getItem(String name) throws Exception {
        ItemStorage storage;
        if ((storage = storageMap.get(name)) != null) {
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
    public Item popItem(String name) throws Exception {
        ItemStorage storage;
        if ((storage = storageMap.get(name)) != null) {
            Item i = storage.items.get(0);
            if (storage.items.size() == 0) {
                storageMap.remove(name);
            }
            notifyListeners();
            return i;
        }
        throw new Exception("Tried to retrieve an item from an empty storage");
    }

    public Set<String> getItems() {
        return storageMap.keySet();
    }

    /**
     * @param itemName
     * @return return the amount of items with the same name as itemName that is currently in storage
     */
    public int getNrOf(String itemName) {
        return storageMap.get(itemName).items.size();
    }

    private void notifyListeners() {
        for (StorageChangeListener scl : listeners) {
            scl.storageChanged();
        }
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
         *
         * @param item
         */
        public ItemStorage(Item item) {
            items.add(item);
            type = item.getName();
            isNew = true;
        }

        /**
         * After a new item has been seen by the user, make it so that it doesn't stand out again.
         *
         * @param isNew
         */
        // TODO change the isNew variable after exiting the Inventory in-game
        public void setNew(boolean isNew) {
            this.isNew = isNew;
        }

        /**
         * Gets, and removes an item from the item list
         *
         * @return
         */
        private Item pop() {
            Item stuff = items.get(0);
            items.remove(0);
            return stuff;
        }
    }
}
