package com.pottda.game.view;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pottda.game.model.Item;

import javax.vecmath.Point2i;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mr Cornholio on 19/05/2017.
 */
public class AtlasCreator {
    public static TextureAtlas atlas = new TextureAtlas();
    public final static int SIZE = 25;
    private static int xLow;
    private static int yLow;

    public static void createAtlas(LinkedList<Item> itemList) {
        for (Item i: itemList) {
            atlas.addRegion(i.getName(), new TextureRegion(createTextureForItem(i)));
        }
    }

    private static Texture createTextureForItem(Item item) {
        List<Point2i> basePos = item.getBasePositions();
        List<Point2i> baseOut = item.getBaseOutputs();

        Pixmap itemPix;
        itemPix = getBoundingBox(basePos, baseOut);

        itemPix.setColor(255, 0, 0, 1);
        for (Point2i i : basePos) {
            itemPix.fillRectangle((i.getX()-xLow)*25, (i.getY()-yLow)*25, SIZE, SIZE);
        }

        itemPix.setColor(0, 0, 255, 1);
        for (Point2i i : baseOut) {
            itemPix.fillRectangle((i.getX()-xLow)*25, (i.getY()-yLow)*25, SIZE, SIZE);
        }

        Texture itemTex = new Texture(itemPix);
        return itemTex;
    }

    private static Pixmap getBoundingBox(List<Point2i> basePos, List<Point2i> baseOut) {
        int xHigh = 0, yHigh = 0, boundingX = 0, boundingY = 0;
        for (Point2i i: basePos) {
            xLow = Math.min(xLow, i.getX());
            xHigh = Math.max(xHigh, i.getX());

            yLow = Math.min(yLow, i.getY());
            yHigh = Math.max(yHigh, i.getY());
        }



        for (Point2i i: baseOut) {
            xLow = Math.min(xLow, i.getX());
            xHigh = Math.max(xHigh, i.getX());

            yLow = Math.min(yLow, i.getY());
            yHigh = Math.max(yHigh, i.getY());
        }

        xHigh -= xLow;
        yHigh -= yLow;

        boundingX = (xHigh+1)*25;
        boundingY = (yHigh+1)*25;
        return new Pixmap(boundingX, boundingY, Pixmap.Format.RGBA8888);
    }
}
