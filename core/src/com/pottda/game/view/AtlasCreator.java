package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
    private final static int SIZE_I = SIZE - 1; // For drawing pixels within SIZE
    private static int xLow;
    private static int yLow;

    private static Pixmap pmPosition;
    private static Pixmap pmOut;

    public static void createAtlas(LinkedList<Item> itemList) {
        pmPosition = new Pixmap(Gdx.files.internal("positionTest.png"));
        pmOut = new Pixmap(Gdx.files.internal("outputTest.png"));
        for (Item i : itemList) {
            atlas.addRegion(i.getName(), new TextureRegion(createTextureForItem(i)));
        }
    }

    private static Texture createTextureForItem(Item item) {
        List<Point2i> basePos = item.getBasePositions();
        List<Point2i> baseOut = item.getBaseOutputs();

        Pixmap itemPix;
        itemPix = createPixmap(item);

        Point2i negativeOffset = item.getBaseBottomLeft();

        itemPix.setColor(Color.BLACK);
        for (Point2i i : basePos) {
            itemPix.drawPixmap(pmPosition,
                    (i.getX() - negativeOffset.x) * SIZE,
                    (i.getY() - negativeOffset.y) * SIZE);
//            itemPix.fillRectangle((i.getX() - xLow) * 25, (i.getY() - yLow) * 25, SIZE, SIZE);
        }

//        itemPix.setColor(0, 0, 255, 1);
        for (Point2i i : baseOut) {
            itemPix.drawPixmap(pmOut,
                    (i.getX() - negativeOffset.x) * SIZE,
                    (i.getY() - negativeOffset.y) * SIZE);
//            itemPix.fillRectangle((i.getX() - xLow) * 25, (i.getY() - yLow) * 25, SIZE, SIZE);
        }

        return new Texture(itemPix);
    }

    private static Pixmap createPixmap(Item i) {
        int boundingX, boundingY;
        Point2i bottomLeft, topRight;

        // Get outlier points
        bottomLeft = i.getBaseBottomLeft();
        topRight = i.getBaseUpperRight();

        // Shift to positive points only
        boundingX = topRight.x - bottomLeft.x;
        boundingY = topRight.y - bottomLeft.y;

        int width = (boundingX + 1) * SIZE;
        int height = (boundingY + 1) * SIZE;

        return new Pixmap(width, height, Pixmap.Format.RGBA8888);
    }
}
