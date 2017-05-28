package com.pottda.game.model;

import com.pottda.game.model.items.ItemSize;
import com.pottda.game.model.items.SizedItem;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        blueprints.put(inventory.getName(), new InventoryBlueprint(inventory));
    }

    // ----------------------------------------------

    private final int width;


    private final int height;

    /**
     * Data structure for saving items
     */
    private final Map<PointAndOrientation, Class<? extends Item>> itemMap;

    private final Map<PointAndOrientation, ItemSize> itemSizeMap;

    private InventoryBlueprint(XMLInventory inventory) {
        itemMap = new HashMap<PointAndOrientation, Class<? extends Item>>();
        itemSizeMap = new HashMap<PointAndOrientation, ItemSize>();
        width = inventory.getWidth();
        height = inventory.getHeight();

        try {
            for (XMLItem i : inventory.getItems()) {
                if (i instanceof XMLSizedItem) {
                    addSizedItemClass(ItemClassLoader.getSizedItemClass(i.getClassName()),
                            i.getX(),
                            i.getY(),
                            i.getOrientation(),
                            ((XMLSizedItem) i).getSize());
                } else {
                    addItemClass(ItemClassLoader.getItemClass(i.getClassName()),
                            i.getX(),
                            i.getY(),
                            i.getOrientation());
                }
            }
        } catch (Exception e) {
            throw new Error("could not instantiate items :", e);
        }
    }

    private void addItemClass(Class<? extends Item> type, int x, int y, int orientation) {
        itemMap.put(new PointAndOrientation(orientation, x, y), type);
    }

    private void addSizedItemClass(Class<? extends SizedItem> type, int x, int y, int orientation, ItemSize size) {
        PointAndOrientation pao = new PointAndOrientation(orientation, x, y);
        itemMap.put(pao, type);
        itemSizeMap.put(pao, size);
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
        Set<Item> itemSet = new HashSet<Item>();

        Item item;
        for (Map.Entry<PointAndOrientation, Class<? extends Item>> entry : itemMap.entrySet()) {
            try {
                item = entry.getValue().getDeclaredConstructor().newInstance();
                if (itemSizeMap.containsKey(entry.getKey())) {
                    ((SizedItem) item).setSize(itemSizeMap.get(entry.getKey()));
                    item.initDynamic();
                }
                item.setX(entry.getKey().x);
                item.setY(entry.getKey().y);
                item.setOrientation(entry.getKey().orientation);
                itemSet.add(item);
            } catch (InstantiationException e) {
                throw new InstantiationError("Could not create instance of: " + entry.getValue().toString());
            } catch (IllegalAccessException e) {
                throw new Error("Access failure in blueprint", e);
            } catch (NoSuchMethodException e) {
                throw new Error("Reflect sorcery failure", e);
            } catch (InvocationTargetException e) {
                throw new Error("Reflect sorcery failure", e);
            }
        }

        inventory.addItems(itemSet);

        return inventory;
    }
}
