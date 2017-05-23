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
    private static Pixmap pmLeftPointer;
    private static Pixmap pmRightPointer;
    private static Pixmap pmDownPointer;
    private static Pixmap pmUpPointer;

    public static void createAtlas(LinkedList<Item> itemList) {
        pmPosition = new Pixmap(Gdx.files.internal("positionTest.png"));
        pmOut = new Pixmap(Gdx.files.internal("outputTest.png"));

        pmLeftPointer = new Pixmap(Gdx.files.internal("leftArrow.png"));
        pmRightPointer = new Pixmap(Gdx.files.internal("rightArrow.png"));
        pmDownPointer = new Pixmap(Gdx.files.internal("downArrow.png"));
        pmUpPointer = new Pixmap(Gdx.files.internal("upArrow.png"));

        for (Item i : itemList) {
            atlas.addRegion(i.getName(), new TextureRegion(createTextureForItem(i)));
        }
        pmPosition.dispose();
        pmOut.dispose();
    }

    private static Texture createTextureForItem(Item item) {
        List<Point2i> basePos = item.getBasePositions();
        List<Point2i> baseOut = item.getBaseOutputs();

        Pixmap itemPix = createPixmap(item);

        Point2i negativeOffset = item.getBaseBottomLeft();

        itemPix.setColor(Color.BLACK);
        for (Point2i p : basePos) {
            int bx = (p.x - negativeOffset.x) * SIZE; // The bottom left X-coordinate (in pixels)
            int by = (p.y - negativeOffset.y) * SIZE; // THe bottom left Y-coordinate (in pixels)

            itemPix.drawPixmap(pmPosition, bx, by);

            // Draw outlines to spaces where there are no neighbouring positions
            if (!basePos.contains(new Point2i(p.x - 1, p.y))) {
                itemPix.drawLine(bx, by, bx, by + SIZE_I);
            }
            if (!basePos.contains(new Point2i(p.x + 1, p.y))) {
                itemPix.drawLine(bx + SIZE_I, by, bx + SIZE_I, by + SIZE_I);
            }
            if (!basePos.contains(new Point2i(p.x, p.y - 1))) {
                itemPix.drawLine(bx, by, bx + SIZE_I, by);
            }
            if (!basePos.contains(new Point2i(p.x, p.y + 1))) {
                itemPix.drawLine(bx, by + SIZE_I, bx + SIZE_I, by + SIZE_I);
            }

            // Draw output arrows in spaces adjacent to outputs
            if (baseOut.contains(new Point2i(p.x - 1, p.y))) {
                itemPix.drawPixmap(pmLeftPointer, bx, by);
            }
            if (baseOut.contains(new Point2i(p.x + 1, p.y))) {
                itemPix.drawPixmap(pmRightPointer, bx, by);
            }
            if (baseOut.contains(new Point2i(p.x, p.y - 1))) {
                itemPix.drawPixmap(pmDownPointer, bx, by);
            }
            if (baseOut.contains(new Point2i(p.x, p.y + 1))) {
                itemPix.drawPixmap(pmUpPointer, bx, by);
            }


//            itemPix.fillRectangle((i.getX() - xLow) * 25, (i.getY() - yLow) * 25, SIZE, SIZE);
        }

//        itemPix.setColor(0, 0, 255, 1);
//        for (Point2i i : baseOut) {
//            itemPix.drawPixmap(pmOut,
//                    (i.getX() - negativeOffset.x) * SIZE,
//                    (i.getY() - negativeOffset.y) * SIZE);
////            itemPix.fillRectangle((i.getX() - xLow) * 25, (i.getY() - yLow) * 25, SIZE, SIZE);
//        }

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
