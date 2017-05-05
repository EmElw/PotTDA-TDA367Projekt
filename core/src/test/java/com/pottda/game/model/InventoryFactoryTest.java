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

//    @Before
//    public void setUp() {
//
//    }

    /**
     * Tests the functionality of the basic build from xml function
     * Uses testInv.xml, which should create two items of type DemoItemA and DemoItemB
     * placing them at specific coordinates
     */
    @Test
    public void inventoryFromXML() {
        String basePath = new File("").getAbsolutePath();

        String filePath = basePath.
                replace("\\core", "").  // No one must know of this blasphemy
                concat("\\android\\assets\\inventoryblueprint\\testInv.xml");
        f = new File(filePath);

        try {
            // Create the inventory
            Inventory i = InventoryFactory.createFromXML(f);
            Assert.assertTrue(i.items.size() == 2);

            // Attempt to retrieve an DemoItemA and DemoItemB from the Inventory
            Item a = findItemA(i);
            Item b = findItemB(i);

            // Test DemoItemA
            Assert.assertFalse(a == null);
            Assert.assertEquals(a.x, 8);
            Assert.assertEquals(a.y, 5);
            Assert.assertEquals(a.orientation, 0);

            // Test DemoItemB
            Assert.assertFalse(b == null);
            Assert.assertEquals(b.x, 3);
            Assert.assertEquals(b.y, 2);
            Assert.assertEquals(b.orientation, 3);

        } catch (Exception e) { // Any exception is probably a fail
            Assert.fail();
            e.printStackTrace();
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
