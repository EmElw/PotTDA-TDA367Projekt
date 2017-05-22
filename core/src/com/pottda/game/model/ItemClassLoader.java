package com.pottda.game.model;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ItemClassLoader {

    /**
     * Map to save String->Class for quicker access (probably)
     */
    private static Map<String, Class> stringClassMap = new HashMap<String, Class>();

    /**
     * Retrieve the @{code Class} corresponding to the given {@code string} from the map
     *
     * @param className the name of the {@code Class} to load
     * @return a Class
     * @throws ClassNotFoundException if the name-tag doesn't correspond to a class
     * @throws IOException            if the name-tag doesn't correspond to a class that is a subclass of Item
     */
    public static Class<? extends Item> getItemClass(String className) throws ClassNotFoundException, IOException {
        if (stringClassMap.containsKey(className)) {
            return stringClassMap.get(className);
        } else {
            Class c;
            c = Class.forName("com.pottda.game.model.items." + className);
            if (isItemSubclass(c)) {
                stringClassMap.put(className, c);
                return stringClassMap.get(className);
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
