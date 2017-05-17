package com.pottda.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.pottda.game.model.items.DemoItemA;
import com.pottda.game.model.items.DemoItemB;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;


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

        // Create the inventory
        Inventory i = null;
        try {
            i = getInventory();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    }

    private Inventory getInventory() throws ClassNotFoundException, ParserConfigurationException, InstantiationException, IllegalAccessException, IOException {
        List<XMLItem> xmlItemList = new ArrayList<XMLItem>();

        FileHandle file = Gdx.files.internal("inventoryblueprint/testInv.xml");
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
