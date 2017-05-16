package com.pottda.game.model;

import com.badlogic.gdx.Gdx;
import com.pottda.game.model.items.ChainAttack;
import com.pottda.game.model.items.MultiShot;
import com.pottda.game.model.items.SimpleCannon;
import com.pottda.game.model.items.Switcher;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.vecmath.Vector2f;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Test some items and Inventory.compile()
 */
public class InventoryTest {
    Inventory testInv2;
    AttackItem cannon;

    @Before
    public void setUp() {
        File xml;
        String basePath = new File("").getAbsolutePath();

        String filePath = basePath.
                replace("\\core", "").  // No one must know of this blasphemy
                concat("\\android\\assets\\inventoryblueprint\\testInv2.xml");
        xml = new File(filePath);

        // Create the inventory to return
        Inventory inventory = new Inventory();

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
            doc = db.parse(xml);

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

        try {
            testInv2 = InventoryFactory.createFromXML(nList, inventory, xml.getName());
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
            File xml;
            Inventory i;
            String basePath = new File("").getAbsolutePath();

            String filePath = basePath.
                    replace("\\core", "").  // No one must know of this blasphemy
                    concat("\\android\\assets\\inventoryblueprint\\illegalTestInv.xml");

            xml = new File(filePath);

            // Create the inventory to return
            Inventory inventory = new Inventory();

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
                doc = db.parse(xml);

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

            try {
                i = InventoryFactory.createFromXML(nList, inventory, xml.getName());
                i.compile();
                Assert.assertFalse(i.isLegal());
            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail();
            }
        }
    }
}