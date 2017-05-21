package com.pottda.game.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magnus on 2017-05-21.
 */
public class XMLInventory {

    private final List<XMLItem> items;
    private final int width;
    private final int height;

    public XMLInventory(List<XMLItem> items, int width, int height) {
        this.items = items;
        this.width = width;
        this.height = height;
    }
}
