package com.pottda.game.model;

import com.pottda.game.model.items.ItemSize;

public class XMLSizedItem extends XMLItem {
    private final ItemSize size;

    public XMLSizedItem(String className, int x, int y, int orientation, int sizeIdentifier) {
        super(className, x, y, orientation);
        size = ItemSize.getSize(sizeIdentifier);
    }

    public ItemSize getSize() {
        return size;
    }
}
