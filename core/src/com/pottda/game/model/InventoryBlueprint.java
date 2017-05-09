package com.pottda.game.model;

import com.pottda.game.model.Inventory;
import com.pottda.game.model.Item;

import javax.vecmath.Point2i;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Class used to cache inventories to speed up instantiating
 */
public class InventoryBlueprint {

    /**
     * The name of the inventory type (usually the same as the file name)
     */
    public final String name;
    public final Map<PointAndOrientation, Class<? extends Item>> itemMap;

    public InventoryBlueprint(String name, Map<PointAndOrientation, Class<? extends Item>> itemMap) {
        this.name = name;
        this.itemMap = itemMap;
    }

    public InventoryBlueprint(String name) {
        this(name, new HashMap<PointAndOrientation, Class<? extends Item>>());
    }

    public void addItem(Class<? extends Item> type, int x, int y, int orientation) {
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
