package com.pottda.game.model;

import java.awt.*;
import java.util.List;

/**
 * Created by rikar on 2017-04-07.
 */

public abstract class Item {
    Point position;
    int orientation;
    //List<VectorType> shape;
    //VectorType output;
    List<Item> nextItem;
}
