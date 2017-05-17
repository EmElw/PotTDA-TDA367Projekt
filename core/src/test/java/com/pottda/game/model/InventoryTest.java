package com.pottda.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.pottda.game.model.items.ChainAttack;
import com.pottda.game.model.items.MultiShot;
import com.pottda.game.model.items.SimpleCannon;
import com.pottda.game.model.items.Switcher;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.vecmath.Vector2f;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Test some items and Inventory.compile()
 */
public class InventoryTest {
    Inventory testInv2;
    AttackItem cannon;
    private Inventory inventory;

    @Before
    public void setUp() {


        try {
            testInv2 = getInventory();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        testInv2.compile();

    }

    @Test
    public void testCompile() {
        Assert.assertEquals(2, testInv2.attackItems.size());
        Assert.assertEquals(6, testInv2.items.size());
    }

    @Test
    public void testCannon() {
        boolean foundSimpleCannon = false;
        for (Item i : testInv2.attackItems) {
            if (i instanceof SimpleCannon) {
                cannon = (AttackItem) i;
                foundSimpleCannon = true;
                Assert.assertTrue(i.getNext() instanceof Switcher);
            }
        }
        Assert.assertTrue(foundSimpleCannon);
    }

    @Test
    public void testSwitcher() {
        boolean foundSwitcher = false;
        for (Item i : testInv2.items) {
            if (i instanceof Switcher) {
                foundSwitcher = true;
                Assert.assertTrue(i.getNext() instanceof ChainAttack);
                Assert.assertTrue(i.getNext() instanceof MultiShot);
            }
        }
        Assert.assertTrue(foundSwitcher);

    }

    @Test
    public void testAttack() {
        for (Item i : testInv2.attackItems) {
            if (i instanceof SimpleCannon) {
                cannon = (AttackItem) i;
            }
        }
        List<ProjectileListener> list = cannon.attack(new Vector2f(), new Vector2f(), 0);

        Assert.assertTrue(list.get(0) instanceof ChainAttack);

        list = cannon.attack(new Vector2f(), new Vector2f(), 0);

        Assert.assertTrue(list.get(0) instanceof MultiShot);
        Assert.assertTrue(list.get(1) instanceof MultiShot);


    }

    @Test
    public void testLegality() {
        boolean isLegal = testInv2.isLegal();
        Assert.assertTrue(isLegal);
        {
            Inventory i;

            try {
                i = getInventory();
                i.compile();
                Assert.assertFalse(i.isLegal());
            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail();
            }
        }
    }

    private Inventory getInventory() throws ClassNotFoundException, ParserConfigurationException, InstantiationException, IllegalAccessException, IOException {
        List<XMLItem> xmlItemList = new ArrayList<XMLItem>();

        FileHandle file = Gdx.files.internal("inventoryblueprint/testInv2.xml");
        // Create the inventory to return
        XmlReader xml = new XmlReader();
        XmlReader.Element xml_element = null;
        try {
            // Read the file
            xml_element = xml.parse(file);
            // If the loaded file does not contain an inventory tag, throw exception
            if (!xml_element.toString().split("\n")[0].contains("inventory")) {
                throw new IOException("Couldn't find <inventory> tag");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        assert xml_element != null;
        String secondLine = xml_element.toString().split("\n")[0];
        // Get w and h from XML file
        final int width = Integer.parseInt(secondLine.split("\"")[1]);
        final int height = Integer.parseInt(secondLine.split("\"")[3]);

        // Set the dimensions of the inventory
        Inventory inventory = new Inventory();
        inventory.setDimensions(width, height);

        // Create the XMLItem list
        for (String s : xml_element.toString().split("\n")) {
            if (s.contains("<item ")) {
                int orientation = Integer.parseInt(s.split("\"")[1]);
                int x = Integer.parseInt(s.split("\"")[3]);
                int y = Integer.parseInt(s.split("\"")[5]);
                String name = s.split("\"")[7];
                XMLItem xmlItem = new XMLItem(name, x, y, orientation);
                xmlItemList.add(xmlItem);
            }
        }

        return InventoryFactory.createFromXML(xmlItemList, inventory, file.name());
    }
}