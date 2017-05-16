package com.pottda.game.model;

import com.badlogic.gdx.Gdx;
import com.pottda.game.model.items.DemoItemA;
import com.pottda.game.model.items.DemoItemB;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
        String basePath = new File("").getAbsolutePath();

        String filePath = basePath.
                replace("\\core", "").  // No one must know of this blasphemy
                concat("\\android\\assets\\inventoryblueprint\\testInv.xml");
        f = new File(filePath);

        try {
            // Create the inventory to return
            Inventory inventory = new Inventory();
            File file = Gdx.files.internal("inventoryblueprint/testInv2.xml").file();


            // Magic loading, based on https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;
            try {
                db = documentBuilderFactory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            Document doc = null;
            try {
                doc = db.parse(file);

                // If the loaded file does not contain an inventory tag, throw exception
                if (!doc.getDocumentElement().getNodeName().equals("inventory")) {
                    throw new IOException("Couldn't find <inventory> tag");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            doc.getDocumentElement().normalize();

            // Set the dimensions of the inventory
            inventory.setDimensions(
                    Integer.parseInt(doc.getDocumentElement().getAttribute("w")),
                    Integer.parseInt(doc.getDocumentElement().getAttribute("h")));

            // Create a list item nodes
            NodeList nList = doc.getElementsByTagName("item");


            // Create the inventory
            Inventory i = InventoryFactory.createFromXML(nList, inventory, file.getName());
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
