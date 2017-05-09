package com.pottda.game.model;

import com.pottda.game.model.Inventory;
import com.pottda.game.model.Item;
import sun.plugin2.util.ParameterNames;

import javax.vecmath.Point2i;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Class used to cache inventories to speed up instantiating
 */
public class InventoryBlueprint {


    /**
     * The name of the inventory type (usually the same as the file name)
     */
    private final String name;
    /**
     * Data structure for saving items
     */
    private final Map<PointAndOrientation, Class<? extends Item>> itemMap;

    private InventoryBlueprint(String name, Map<PointAndOrientation, Class<? extends Item>> itemMap) {
        this.name = name;
        this.itemMap = itemMap;
    }

    /**
     * Creates a blueprint from a given {@link Inventory}.
     * <p>
     * Does not actually copy items, but maps a position/orientation to a class
     *
     * @param name      a {@link String}
     * @param inventory a {@link Inventory} with one or more {@link Item}
     */
    public InventoryBlueprint(String name, Inventory inventory) throws Exception {
        this(name, new HashMap<PointAndOrientation, Class<? extends Item>>());
        if (inventory.items.isEmpty())
            throw new Exception("Inventory is empty: " + inventory.toString());
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
