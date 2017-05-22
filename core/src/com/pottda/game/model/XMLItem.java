package com.pottda.game.model;

/**
 * Created by Rikard Teodorsson on 2017-05-17.
 */

public class XMLItem {
    private final String className;
    private final int x;
    private final int y;
    private final int orientation;

    public XMLItem(String className, int x, int y, int orientation) {
        this.className = className;
        this.x = x;
        this.y = y;
        this.orientation = orientation;
    }

    String getClassName() {
        return className;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getOrientation() {
        return orientation;
    }

}
