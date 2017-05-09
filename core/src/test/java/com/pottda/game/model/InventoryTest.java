package com.pottda.game.model;

import com.pottda.game.model.items.ChainAttack;
import com.pottda.game.model.items.MultiShot;
import com.pottda.game.model.items.SimpleCannon;
import com.pottda.game.model.items.Switcher;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.vecmath.Vector2f;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test some items and Inventory.compile()
 */
public class InventoryTest {
    Inventory inventory;
    AttackItem cannon;

    @Before
    public void setUp() {
        File xml;
        String basePath = new File("").getAbsolutePath();

        String filePath = basePath.
                replace("\\core", "").  // No one must know of this blasphemy
                concat("\\android\\assets\\inventoryblueprint\\testInv2.xml");
        xml = new File(filePath);
        try {
            inventory = InventoryFactory.createFromXML(xml);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCompile() {
        Assert.assertEquals(1, inventory.attackItems.size());
        Assert.assertEquals(6, inventory.items.size());
    }

    @Test
    public void testCannon() {
        boolean foundSimpleCannon = false;
        for (Item i : inventory.attackItems) {
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
        for (Item i : inventory.items) {
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
        for (Item i : inventory.attackItems) {
            if (i instanceof SimpleCannon) {
                cannon = (AttackItem) i;
            }
        }
        List<ProjectileListener> list = cannon.attack(new Vector2f(), new Vector2f());

        Assert.assertTrue(list.get(0) instanceof ChainAttack);

        list = cannon.attack(new Vector2f(), new Vector2f());

        Assert.assertTrue(list.get(0) instanceof MultiShot);
        Assert.assertTrue(list.get(1) instanceof MultiShot);


    }
}