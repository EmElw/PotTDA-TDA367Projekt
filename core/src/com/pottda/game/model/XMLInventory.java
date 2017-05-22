package com.pottda.game.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magnus on 2017-05-21.
 */
public class XMLInventory {

    public final String name;
    public final List<XMLItem> items;
    public final int width;
    public final int height;

    public XMLInventory(String name, List<XMLItem> items, int width, int height) {
        this.name = name;
        this.items = items;
        this.width = width;
        this.height = height;
    }
}
