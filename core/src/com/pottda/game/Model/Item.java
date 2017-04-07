package com.pottda.game.Model;

import java.awt.Point;
import java.util.List;
import java.util.Vector;

/**
 * Created by rikar on 2017-04-07.
 */

public abstract class Item {
    Point position;
    int orientation;
    List<Vector> shape;
    Vector output;
    List<Item> nextItem;
}
