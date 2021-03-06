package com.pottda.game.model;

import java.util.*;

public class Storage {

    private final List<StorageChangeListener> listeners;

    /**
     * A map with a String key (for the name of the item to be added) and an ItemStorage
     */
    private final Map<String, ItemStorage> storageMap;


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
    public Item popItem(String name) {
        ItemStorage storage;
        if ((storage = storageMap.get(name)) != null) {
            Item i = storage.items.get(0);
            storage.items.remove(0);
            if (storage.items.size() == 0) {
                storageMap.remove(name);
            }
            notifyListeners();
            return i;
        }
        throw new Error("Tried to retrieve an item from an empty storage");
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

    public void addStorageChangeListener(StorageChangeListener storageChangeListener) {
        listeners.add(storageChangeListener);
    }

    public void removeStorageChangeListener(StorageChangeListener storageChangeListener) {
        listeners.remove(storageChangeListener);
    }

    public void unflagNewItems() {
        for (ItemStorage im : storageMap.values()) {
            im.setNew(false);
        }
    }

    public boolean isNewItemInStorage(String itemName) {
        if(storageMap.containsKey(itemName))
            return storageMap.get(itemName).isNew;
        return false;
    }

    public void addItems(Collection<Item> items) {
        for (Item item : items) {
            if (storageMap.containsKey(item.getName())) {
                storageMap.get(item.getName()).items.add(item);
            } else {
                storageMap.put(item.getName(), new ItemStorage(item));
            }
        }
        notifyListeners();
    }

    /**
     * ItemStorage holds one specific item and keeps additional statistics of it
     */
    public class ItemStorage {
        /**
         * A list to store items of a certain name
         */
        private final List<Item> items = new ArrayList<Item>();

        /**
         * Boolean used to check if the item is a new addition to storage
         */
        private boolean isNew;

        /**
         * String to check what kind of item it is (its name)
         */
        private final String type;

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
