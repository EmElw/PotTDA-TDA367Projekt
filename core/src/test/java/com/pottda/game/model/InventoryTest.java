package com.pottda.game.model;

import com.badlogic.gdx.files.FileHandle;
import com.pottda.game.application.MyXMLReader;
import com.pottda.game.model.builders.AbstractModelBuilder;
import com.pottda.game.model.items.ChainAttack;
import com.pottda.game.model.items.MultiShot;
import com.pottda.game.model.items.SimpleCannon;
import com.pottda.game.model.items.Switcher;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import javax.vecmath.Vector2f;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static com.pottda.game.model.InventoryBlueprint.getInventory;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test some items and Inventory.compile()
 */
//@RunWith(GdxTestRunner.class)
public class InventoryTest {
    private Inventory testInv2;
    private AttackItem cannon;
    private static final MyXMLReader reader = new MyXMLReader();

    @BeforeClass
    public static void setUpUp() {

        String basePath = new File("").getAbsolutePath();
        String filePath = basePath.
                replace("\\core", "").  // No one must know of this blasphemy
                concat("\\android\\assets\\inventoryblueprint");

        FileHandle file = new FileHandle(filePath);
        generateInventories(file, reader);


        AbstractModelBuilder.setPhysiscActorFactory(new PhysicsActorFactory() {

            final PhysicsActor pa = new PhysicsActor() {
                @Override
                public Vector2f getPosition() {
                    return null;
                }

                @Override
                public void giveMovementVector(Vector2f movementVector) {

                }

                @Override
                public void destroyBody() {

                }

                @Override
                public void setPosition(Vector2f position) {

                }
            };

            @Override
            public PhysicsActor getProjectilePhysicsActor(Projectile projectile) {
                return pa;
            }

            @Override
            public PhysicsActor getCharacterPhysicsActor(Character character) {
                return pa;
            }

            @Override
            public PhysicsActor getObstaclePhysicsActor(Obstacle obstacle) {
                return pa;
            }
        });
    }

    @Before
    public void setUp() {


        try {
            testInv2 = getInventory("testInv2.xml");
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
                assertTrue(i.getNext() instanceof Switcher);
            }
        }
        assertTrue(foundSimpleCannon);
    }

    @Test
    public void testSwitcher() {
        boolean foundSwitcher = false;
        for (Item i : testInv2.items) {
            if (i instanceof Switcher) {
                foundSwitcher = true;
                assertTrue(i.getNext() instanceof ChainAttack);
                assertTrue(i.getNext() instanceof MultiShot);
            }
        }
        assertTrue(foundSwitcher);

    }

    @Test
    public void testAttack() {
        for (Item i : testInv2.attackItems) {
            if (i instanceof SimpleCannon) {
                cannon = (AttackItem) i;
            }
        }
        List<ProjectileListener> list = cannon.attack(new Vector2f(), new Vector2f(), 0);

        assertTrue(list.get(0) instanceof ChainAttack);

        list = cannon.attack(new Vector2f(), new Vector2f(), 0);

        assertTrue(list.get(0) instanceof MultiShot);
        assertTrue(list.get(1) instanceof MultiShot);


    }

    @Test
    public void testLegality() {
        boolean isLegal = testInv2.isLegal();
        assertTrue(isLegal);
        {
            Inventory i;

            try {
                i = getInventory("illegalTestInv.xml");
                i.compile();
                Assert.assertFalse(i.isLegal());
            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail();
            }
        }
    }

    @Test
    public void testInsertLegality() {
        Item switcher = new Switcher();
        // Test add to irrelevant space: (no loops, no overlap) legal -> legal
        assertTrue(testInv2.itemLegalAt(4, 4, 0, switcher));

        // Test add : (with loops, no overlap) legal -> illegal
        assertFalse(testInv2.itemLegalAt(5, 0, 2, switcher));

        // Test add : (no loops, with overlap) legal -> illegal
        assertFalse(testInv2.itemLegalAt(6, 3, 0, switcher));

        // Test add : (with loops, with overlap) legal -> illegal
        assertFalse(testInv2.itemLegalAt(5, 0, 1, switcher));

        // Test add to irrelevant space: (with loops, with overlap) illegal -> illegal
        switcher.setOrientation(1);
        switcher.setX(5);
        switcher.setY(0);

        Item simpleCannon = new SimpleCannon();

        testInv2.addItem(switcher);
        assertFalse(testInv2.itemLegalAt(6, 6, 0, simpleCannon));
    }

    private static void generateInventories(FileHandle folder, MyXMLReader reader) {

        List<FileHandle> contents = Arrays.asList(folder.list("xml"));
        try {
            for (FileHandle f : contents) {
                InventoryBlueprint.newBlueprint(reader.parseInventory(f));
            }
        } catch (Exception e) {
            throw new Error("failed to generate inventory blueprints: ", e);
        }
    }

}