package com.pottda.game.model;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.pottda.game.controller.AbstractController;
import com.pottda.game.controller.Box2DActorFactory;
import com.pottda.game.controller.ProjectileController;
import com.pottda.game.model.items.ChainAttack;
import com.pottda.game.model.items.MultiShot;
import com.pottda.game.model.items.SimpleCannon;
import com.pottda.game.model.items.Switcher;
import com.pottda.game.view.Sprites;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import testrunner.GdxTestRunner;

import javax.vecmath.Vector2f;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Test some items and Inventory.compile()
 */
//@RunWith(GdxTestRunner.class)
public class InventoryTest {
    Inventory testInv2;
    AttackItem cannon;

    @Before
    public void setUp() {
        File xml;


        ActorFactory.setFactory(new ActorFactory() {
            @Override
            public AbstractController buildEnemy(Sprites sprite, Vector2f position, Inventory inventory) {
                return null;
            }

            @Override
            public AbstractController buildPlayer(Sprites sprite, Vector2f position) {
                return null;
            }

            @Override
            public AbstractController buildProjectile(Sprites sprite, int team, boolean bounces, boolean penetrates, Vector2f position) {
                PhysicsActor pa = new PhysicsActor() {
                    @Override
                    public Vector2f getPosition() {
                        return new Vector2f();
                    }

                    @Override
                    public void giveMovementVector(Vector2f movementVector) {

                    }

                    @Override
                    public void destroyBody() {

                    }
                };

                Projectile p = new Projectile(pa, 0, new ArrayList<ProjectileListener>());

                return new AbstractController(p, null) {
                    @Override
                    protected void setInputVectors() {

                    }
                };
            }

            @Override
            public AbstractController buildObstacle(Sprites sprite, Vector2f position, Vector2f size, boolean isBorder) {
                return null;
            }
        });
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