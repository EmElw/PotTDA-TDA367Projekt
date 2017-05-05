package com.pottda.game.model;

import javax.vecmath.Point2i;
import javax.vecmath.Vector2f;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rikar on 2017-04-07.
 */

public abstract class Item {
    int orientation;

    List<Point2i> positions;

    public void init() {
        positions = new ArrayList<Point2i>();
    }

    public int getOrientation() {
        return orientation;
    }

    public int getOrientetationn() {
        return orientetationn;
    }

    List<Item> nextItem;
    public int x;
    public int y;
    private int orientetationn;


    public void setOrientetationn(int orientetationn) {
        this.orientetationn = orientetationn;
    }
}
