package com.pottda.game.Model;

import java.awt.Point;
import java.util.List;

/**
 * Created by rikar on 2017-04-07.
 */

public abstract class Item {
    Point position;
    int orientation;
    List<VectorType> shape;
    VectorType output;
    List<Item> nextItem;
}
