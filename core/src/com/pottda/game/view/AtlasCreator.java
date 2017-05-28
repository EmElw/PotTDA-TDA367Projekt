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
import java.util.Set;

/**
 * Class for generating a {@code TextureAtlas} from {@link Item}
 */
public class AtlasCreator {
    public final static int SIZE = 25;          // The base
    private final static int SIZE_I = SIZE - 1; // For drawing pixels within SIZE

    /**
     * A {@code TextureAtlas} showing different Items.
     * <p>
     * The region-names are the samea s the Item-names.
     */
    public static final TextureAtlas atlas = new TextureAtlas();

    /**
     * Creates a {@link TextureAtlas}
     *
     * @param itemList
     */
    public static void createAtlas(LinkedList<Item> itemList) {

        String name;
        for (Item item : itemList) {
            try {
                name = item.getName();
                atlas.addRegion(name, new TextureRegion(createTextureForItem(item)));
                TextureAtlas.AtlasRegion r = atlas.findRegion(name);
                r.flip(false, true);
            } catch (Exception e) {
                System.out.println("u dun goofd");
            }
        }

    }

    /**
     * Internal method to create a texture from a given {@link Item}
     *
     * @param item an {@link Item}
     * @return a {@link Texture} showing the item
     */
    private static Texture createTextureForItem(Item item) {
        // Create pixmaps from resources to assemble into the images
        Pixmap pmBackground = new Pixmap(Gdx.files.internal("positionTest.png"));
        Pixmap pmOut = new Pixmap(Gdx.files.internal("outputTest.png"));
        Pixmap pmLeftPointer = new Pixmap(Gdx.files.internal("leftArrow.png"));
        Pixmap pmRightPointer = new Pixmap(Gdx.files.internal("rightArrow.png"));
        Pixmap pmDownPointer = new Pixmap(Gdx.files.internal("downArrow.png"));
        Pixmap pmUpPointer = new Pixmap(Gdx.files.internal("upArrow.png"));

        // Retrieve position data from the item
        Set<Point2i> basePos = (Set<Point2i>) item.getBasePositions();
        Set<Point2i> baseOut = (Set<Point2i>) item.getBaseOutputs();

        // Create the pixmap to draw onto
        Pixmap itemPix = createPixmap(item);

        // The required offset to ensure that all coordinates in the items are >= 0
        Point2i negativeOffset = item.getBaseBottomLeft();

        itemPix.setColor(Color.BLACK);

        for (Point2i p : basePos) {
            int bx = (p.x - negativeOffset.x) * SIZE; // The bottom left X-coordinate (in pixels)
            int by = (p.y - negativeOffset.y) * SIZE; // THe bottom left Y-coordinate (in pixels)

            // Draw the cell background
            itemPix.drawPixmap(pmBackground, bx, by);

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
        }

        // Dispose of pixmaps
        pmBackground.dispose();
        pmOut.dispose();
        pmLeftPointer.dispose();
        pmRightPointer.dispose();
        pmDownPointer.dispose();
        pmUpPointer.dispose();


        return new Texture(itemPix);
    }

    /**
     * Internal method for creating a {@link Pixmap} from a given {@link Item}
     * <p>
     * This method does not do any drawing, rather it creates an appropriately sized {@code Pixmap}
     *
     * @param i an {@link Item}
     * @return a {@link Pixmap} with a bounding box based on the {@code Item}'s coordinates
     */
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
