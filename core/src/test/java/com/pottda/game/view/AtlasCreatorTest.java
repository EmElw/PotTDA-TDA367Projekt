package com.pottda.game.view;

import com.pottda.game.model.Item;
import com.pottda.game.model.items.MultiShot;
import com.pottda.game.model.items.SimpleCannon;
import com.pottda.game.view.AtlasCreator;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;

/**
 * Created by Mr Cornholio on 19/05/2017.
 */
public class AtlasCreatorTest {
    Item simpleCannon;
    Item multiShot;
    AtlasCreator atlas;
    BufferedImage img;

    @Before
    public void setup() throws Exception{
        multiShot = new MultiShot();
        simpleCannon = new SimpleCannon();
        atlas = new AtlasCreator();
    }

    @Test
    public void BoundingBoxTest() throws Exception {
        atlas.createImageFromItem(simpleCannon);
        img = atlas.getImg();
        System.out.println(img.getWidth());
        System.out.println(img.getHeight());

        atlas.createImageFromItem(multiShot);
        img = atlas.getImg();
        System.out.println(img.getWidth());
        System.out.println(img.getHeight());

    }
}
