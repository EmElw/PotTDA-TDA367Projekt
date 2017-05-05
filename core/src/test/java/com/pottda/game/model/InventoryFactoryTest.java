package com.pottda.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.pottda.game.MyGame;
import com.pottda.game.model.items.DemoItemA;
import com.pottda.game.model.items.DemoItemB;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;


public class InventoryFactoryTest {

    File f;

    /**
     * Tests the functionality of the basic build from xml function
     * Uses testInv.xml, which should create two items of type DemoItemA and DemoItemB
     * placing them at specific coordinates
     */
//    @Before
//    public void setUp() {
//
//    }

    @Test
    public void inventoryFromXML() {
        String basePath = new File("").getAbsolutePath();
        String filePath = basePath.concat("\\android\\assets\\inventoryblueprint\\testInv.xml");
        f = new File(filePath);
        //f = new File("C:\\Users\\Magnus\\IdeaProjects\\TDA367Proj\\PotTDA-TDA367Projekt\\android\\assets\\inventoryblueprint\\testInv.xml");

        try {
            Inventory i = InventoryFactory.createFromXML(f);
            Assert.assertTrue(i.items.size() == 2);

            Item a = findItemA(i);
            Item b = findItemB(i);

            if (a != null) {
                Assert.assertEquals(a.x, 8);
                Assert.assertEquals(a.y, 5);
                Assert.assertEquals(a.orientation, 0);
            }
            if (b != null) {
                Assert.assertEquals(b.x, 3);
                Assert.assertEquals(b.y, 2);
                Assert.assertEquals(b.orientation, 3);
            }

            Assert.assertFalse(a == null);
            Assert.assertFalse(b == null);


        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }


    private Item findItemA(Inventory i) {
        for (Item item : i.items) {
            if (item instanceof DemoItemA)
                return item;
        }
        return null;
    }

    private Item findItemB(Inventory i) {
        for (Item item : i.items) {
            if (item instanceof DemoItemB)
                return item;
        }
        return null;
    }

}
