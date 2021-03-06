package com.pottda.game.model;

import com.pottda.game.model.items.SizedItem;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


class ItemClassLoader {

    /**
     * Map to save String->Class for quicker access (probably)
     */
    private static final Map<String, Class> stringClassMap = new HashMap<String, Class>();

    /**
     * Retrieve the @{code Class} corresponding to the given {@code string} from the map
     *
     * @param className the name of the {@code Class} to load
     * @return a Class
     * @throws ClassNotFoundException if the name-tag doesn't correspond to a class
     * @throws IOException            if the name-tag doesn't correspond to a class that is a subclass of Item
     */
    static Class getItemClass(String className) throws ClassNotFoundException, IOException {
        if (stringClassMap.containsKey(className)) {
            return stringClassMap.get(className);
        } else {
            Class c;
            c = Class.forName("com.pottda.game.model.items." + className);
            if (isItemSubclass(c)) {
                stringClassMap.put(className, c);
                return stringClassMap.get(className);
            } else {
                throw new IOException("name-tag doesn't correspond to an Item");
            }
        }
    }

    /**
     * Retrieve the @{code Class} corresponding to the given {@code string} from the map
     *
     * @param className the name of the {@code Class} to load
     * @return a Class
     * @throws ClassNotFoundException if the name-tag doesn't correspond to a class
     * @throws IOException            if the name-tag doesn't correspond to a class that is a subclass of Item
     */
    static Class getSizedItemClass(String className) throws ClassNotFoundException, IOException {
        if(stringClassMap.containsKey(className)){
            return stringClassMap.get(className);
        } else {
            Class c;
            c = Class.forName("com.pottda.game.model.items." + className);
            if (isSizedItemSubclass(c)) {
                stringClassMap.put(className, c);
                return stringClassMap.get(className);
            } else {
                throw new IOException("name-tag doesn't correspond to a SizedItem");
            }
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

    /**
     * Returns true if the given {@code Class} is a subclass of {@code SizedItem}
     *
     * @param c the {@code Class} to check
     * @return boolean
     */
    private static boolean isSizedItemSubclass(Class c) {
        Class temp = c;
        while (!c.equals(Object.class)) {
            try {
                if ((temp = temp.getSuperclass()) == SizedItem.class) {
                    return true;
                }
            } catch (NullPointerException e){
                throw new NullPointerException("Failed when searching: " + c.toString());
            }
        }
        return false;
    }
}
