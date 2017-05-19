package com.pottda.game.view;

import com.pottda.game.model.Item;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Mr Cornholio on 19/05/2017.
 */
public class AtlasCreator {
    BufferedImage img;

    public void createImageFromItem(Item item) {
        img = new BufferedImage(125,125, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();


    }
}
