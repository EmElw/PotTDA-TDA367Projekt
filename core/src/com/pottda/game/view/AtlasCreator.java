package com.pottda.game.view;

import com.pottda.game.model.Item;

import javax.vecmath.Point2i;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by Mr Cornholio on 19/05/2017.
 */
public class AtlasCreator {
    private BufferedImage img;
    private final static int size = 25;

    public void createImageFromItem(Item item) {
        List<Point2i> basePos = item.getBasePositions();
        List<Point2i> baseOut = item.getBaseOutputs();

        img = getBoundingBox(basePos, baseOut);
        Graphics g = img.getGraphics();


    }

    private BufferedImage getBoundingBox(List<Point2i> basePos, List<Point2i> baseOut) {
        int xLow = 0, xHigh = 0, yLow = 0, yHigh = 0, boundingX = 0, boundingY = 0;
        for (Point2i i: basePos) {
            xLow = Math.min(xLow, i.getX());
            xHigh = Math.max(xHigh, i.getX()+1);

            yLow = Math.min(yLow, i.getY());
            yHigh = Math.max(yHigh, i.getY()+1);
        }



        for (Point2i i: baseOut) {
            xLow = Math.min(xLow, i.getX());
            xHigh = Math.max(xHigh, i.getX()+1);

            yLow = Math.min(yLow, i.getY());
            yHigh = Math.max(yHigh, i.getY()+1);
        }

        boundingX = (xHigh - xLow)*25;
        boundingY = (yHigh - yLow)*25;
        return new BufferedImage(boundingX, boundingY, BufferedImage.TYPE_INT_ARGB);
    }

    public BufferedImage getImg() {
        return  img;
    }
}
