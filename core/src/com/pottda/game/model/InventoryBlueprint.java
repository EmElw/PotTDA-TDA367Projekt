package com.pottda.game.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Class used to cache inventories to speed up instantiating
 */
public class InventoryBlueprint {

    private static final Map<String, InventoryBlueprint> blueprints = new HashMap<String, InventoryBlueprint>();

    /**
     * Returns an {@link Inventory} for the given name
     *
     * @param name a {@link String}
     * @return an {@link Inventory}
     */
    public static Inventory getInventory(String name) {
        return blueprints.get(name).newInventory();
    }

    /**
     * Returns true if there is a blueprint for the given name
     *
     * @param name a {@link String}
     * @return true if there is a blueprint for the given name
     */
    public static boolean contains(String name) {
        return blueprints.containsKey(name);
    }

    /**
     * Creates and adds a new blueprint from an {@link XMLInventory}
     *
     * @param inventory an {@link XMLInventory}
     */
    public static void newBlueprint(XMLInventory inventory) {
        blueprints.put(inventory.name, new InventoryBlueprint(inventory));
    }

    // ----------------------------------------------

    private final int width;


    private final int height;

    /**
     * Data structure for saving items
     */
    private final Map<PointAndOrientation, Class<? extends Item>> itemMap;

    private InventoryBlueprint(XMLInventory inventory) {
        itemMap = new HashMap<PointAndOrientation, Class<? extends Item>>();
        width = inventory.width;
        height = inventory.height;

        try {
            for (XMLItem i : inventory.items) {
                addItemClass(ItemClassLoader.getItemClass(i.getClassName()),
                        i.getX(),
                        i.getY(),
                        i.getOrientation());
            }
        } catch (Exception e) {
            throw new Error("could not instantiate items :", e);
        }
    }

    private void addItemClass(Class<? extends Item> type, int x, int y, int orientation) {
        itemMap.put(new PointAndOrientation(orientation, x, y), type);

    }

    private class PointAndOrientation {
        private final int orientation;
        private final int x;
        private final int y;

        PointAndOrientation(int orientation, int x, int y) {
            this.orientation = orientation;
            this.x = x;
            this.y = y;
        }
    }

    private Inventory newInventory() {
        Inventory inventory = new Inventory();
        inventory.setDimensions(width, height);
        Item item;
        for (Map.Entry<PointAndOrientation, Class<? extends Item>> entry : itemMap.entrySet()) {
            try {
                item = entry.getValue().newInstance();
                item.init();
                item.setX(entry.getKey().x);
                item.setY(entry.getKey().y);
                item.setOrientation(entry.getKey().orientation);
                inventory.addItem(item);
            } catch (InstantiationException e) {
                throw new InstantiationError("Could not create instance of: " + entry.getValue().toString());
            } catch (IllegalAccessException e) {
                throw new Error("Access failure in blueprint", e);
            }
        }

        return inventory;
    }
}
