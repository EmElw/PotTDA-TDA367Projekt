package com.pottda.game.view;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.pottda.game.model.Item;

import javax.vecmath.Point2i;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mr Cornholio on 19/05/2017.
 */
public class AtlasCreator {
    public static TextureAtlas atlas = new TextureAtlas();
    private final static int size = 25;

    public static void createAtlas(LinkedList<Item> itemList) {
        for (Item i: itemList) {
            atlas.addRegion(i.name, new TextureRegion(createTextureForItem(i)));
        }
    }

    private static Texture createTextureForItem(Item item) {
        List<Point2i> basePos = item.getBasePositions();
        List<Point2i> baseOut = item.getBaseOutputs();

        Pixmap itemPix;
        itemPix = getBoundingBox(basePos, baseOut);

        itemPix.setColor(255, 0, 0, 1);
        for (Point2i i : basePos) {
            itemPix.fillRectangle(i.getX()*25, i.getY()*25, size, size);
        }

        itemPix.setColor(0, 0, 255, 1);
        for (Point2i i : baseOut) {
            itemPix.fillRectangle(i.getX()*25, i.getY()*25, size, size);
        }

        Texture itemTex = new Texture(itemPix);
        return itemTex;
    }

    private static Pixmap getBoundingBox(List<Point2i> basePos, List<Point2i> baseOut) {
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
        return new Pixmap(boundingX, boundingY, Pixmap.Format.RGBA8888);
    }
}
