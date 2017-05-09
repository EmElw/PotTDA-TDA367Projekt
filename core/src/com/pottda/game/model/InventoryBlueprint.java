package com.pottda.game.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Class used to cache inventories to speed up instantiating
 */
public class InventoryBlueprint {

    /**
     * Returns a new instance of an {@link Inventory} that corresponds to the given name.
     *
     * @param name a {@link String} with the name of the Inventory
     * @return a new {@link Inventory}
     * @throws IllegalAccessException if {@link Class}.newInstance() fails.
     */
    public static Inventory getForName(String name) throws Exception {
        return blueprints.get(name).newInventory();
    }

    /**
     * Create a new blueprint and assign it to the given name
     *
     * @param name
     * @param i
     * @throws Exception
     */
    public static void createBlueprint(String name, Inventory i) throws Exception {
        blueprints.put(name, new InventoryBlueprint(i));
    }

    private static Map<String, InventoryBlueprint> blueprints;

    /**
     * Data structure for saving items
     */
    private final Map<PointAndOrientation, Class<? extends Item>> itemMap;

    private InventoryBlueprint(Map<PointAndOrientation, Class<? extends Item>> itemMap) {
        this.itemMap = itemMap;
    }

    /**
     * Creates a blueprint from a given {@link Inventory}.
     * <p>
     * Does not actually copy items, but maps a position/orientation to a class
     *
     * @param inventory a {@link Inventory} with one or more {@link Item}
     */
    private InventoryBlueprint(Inventory inventory) throws Exception {
        this(new HashMap<PointAndOrientation, Class<? extends Item>>());
        if (inventory.items.isEmpty()) {
            throw new Exception("Inventory is empty: " + inventory.toString());
        }
        for (Item i : inventory.items) {
            addItemClass(i.getClass(), i.x, i.y, i.orientation);
        }
    }

    private void addItemClass(Class<? extends Item> type, int x, int y, int orientation) {
        itemMap.put(new PointAndOrientation(orientation, x, y), type);

    }

    private class PointAndOrientation {
        private final int orientation;
        private final int x;
        private final int y;

        public PointAndOrientation(int orientation, int x, int y) {
            this.orientation = orientation;
            this.x = x;
            this.y = y;
        }
    }

    public Inventory newInventory() throws IllegalAccessException {
        Inventory inventory = new Inventory();
        Item item;
        for (Map.Entry<PointAndOrientation, Class<? extends Item>> entry : itemMap.entrySet()) {
            try {
                item = entry.getValue().newInstance();
                item.x = entry.getKey().x;
                item.y = entry.getKey().y;
                item.orientation = entry.getKey().orientation;
                inventory.addItem(item);
            } catch (InstantiationException e) {
                throw new InstantiationError("Could not create instance of: " + entry.getValue().toString());
            } catch (IllegalAccessException e) {
                throw new IllegalAccessException("Could not access: " + entry.getValue().toString());
            }
        }

        return inventory;
    }
}
