package com.pottda.game.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class InventoryFactory {

    /**
     * Map to save String->Class for quicker access (probably)
     */
    private static Map<String, Class> stringClassMap = new HashMap<String, Class>();

    /**
     * Creates an inventory using a .xml-file
     *
     * @return an Inventory built from the .xml-file
     * @throws ParserConfigurationException as per {@code DocumentBuilderFactory.newDocumentBuild}
     * @throws IOException                  if the file doesn't contain an inventory-tag or,
     *                                      if the name-tag doesn't correspond to an Item
     * @throws ClassNotFoundException       if the name-tag doesn't correspond to a Class
     * @throws IllegalAccessException       as per {@code Class.newInstance}
     * @throws InstantiationException       as per {@code Class.newInstance}
     */
    public static Inventory createFromXML(NodeList nList, Inventory inventory, String name) throws ParserConfigurationException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        if (InventoryBlueprint.hasInventory(name)) {
            inventory = InventoryBlueprint.getInventory(name);
            inventory.compile();
            return inventory;
        }

        for (int idx = 0; idx < nList.getLength(); idx++) {
            // Get the item as an element from the list
            Element element = (Element) nList.item(idx);

            // Try to load the item type given by the file
            String className = element.getAttribute("name");
            Item item = (Item) getClass(className).newInstance();   // TODO fix so that it uses constructor instead
            item.init();
            // Set properties of the item given by the file
            item.x = (Integer.parseInt(element.getAttribute("x")));
            item.y = (Integer.parseInt(element.getAttribute("y")));
            item.orientation = (Integer.parseInt(element.getAttribute("orientation")));

            // Add the item to the inventory
            inventory.addItem(item);
        }

        // Add to cached blueprints
        InventoryBlueprint.createBlueprint(name, inventory);
        inventory.compile();
        return inventory;

    }

    /**
     * Retrieve the @{code Class} corresponding to the given {@code string} from the map
     *
     * @param className the name of the {@code Class} to load
     * @return a Class
     * @throws ClassNotFoundException if the name-tag doesn't correspond to a class
     * @throws IOException            if the name-tag doesn't correspond to a class that is a subclass of Item
     */
    private static Class getClass(String className) throws ClassNotFoundException, IOException {
        if (stringClassMap.containsKey(className)) {
            return stringClassMap.get(className);
        } else {
            Class c;
            c = Class.forName("com.pottda.game.model.items." + className);
            if (isItemSubclass(c)) {
                stringClassMap.put(className, c);
                return getClass(className);
            } else throw new IOException("name-tag doesn't correspond to an Item");
        }
    }

    /**
     * Returns true if the given {@code Class} is a subclass of {@code Item}
     *
     * @param c the {@code Class} to check
     * @return boolean
     */
    private static boolean isItemSubclass(Class c) {
        Class temp = c;
        while (!c.equals(Object.class)) {
            try {
                if ((temp = temp.getSuperclass()) == Item.class) {
                    return true;
                }
            } catch (NullPointerException e) {
                throw new NullPointerException("Failed when searching: " + c.toString());
            }
        }
        return false;
    }

}
