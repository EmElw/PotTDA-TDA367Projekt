package com.pottda.game.model;

import com.pottda.game.model.items.ChainAttack;
import com.pottda.game.model.items.MultiShot;
import com.pottda.game.model.items.SimpleCannon;
import com.pottda.game.model.items.Switcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.vecmath.Vector2f;
import java.io.File;
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

        try {
            testInv2 = InventoryFactory.createFromXML(xml);
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
        List<ProjectileListener> list = cannon.attack(new Vector2f(), new Vector2f());

        Assert.assertTrue(list.get(0) instanceof ChainAttack);

        list = cannon.attack(new Vector2f(), new Vector2f());

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
            try {
                i = InventoryFactory.createFromXML(xml);
                i.compile();
                Assert.assertFalse(i.isLegal());
            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail();
            }
        }
    }
}