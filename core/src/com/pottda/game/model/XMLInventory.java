package com.pottda.game.model;

import java.util.List;

/**
 * Created by Magnus on 2017-05-21.
 */
public class XMLInventory {

    private final String name;
    private final List<XMLItem> items;
    private final int width;
    private final int height;

    public XMLInventory(String name, List<XMLItem> items, int width, int height) {
        this.name = name;
        this.items = items;
        this.width = width;
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public List<XMLItem> getItems() {
        return items;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
