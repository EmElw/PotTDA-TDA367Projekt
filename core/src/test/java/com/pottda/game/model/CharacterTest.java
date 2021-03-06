package com.pottda.game.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.vecmath.Point2i;
import javax.vecmath.Vector2f;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CharacterTest {
    private Character character;
    private PhysicsActor fakePhysicsActor;

    @Before
    public void setUp() throws Exception {
        fakePhysicsActor = new PhysicsActor() {
            public Vector2f position = new Vector2f();

            @Override
            public Vector2f getPosition() {
                position.normalize();
                return position;
            }

            @Override
            public void giveMovementVector(Vector2f movementVector) {
                position.set(movementVector);
            }

            @Override
            public void destroyBody() {

            }

            @Override
            public void setPosition(Vector2f position) {

            }
        };
        character = new Character();
        character.setPhysicsActor(fakePhysicsActor);
    }

    @After
    public void tearDown() throws Exception {
        fakePhysicsActor = null;
        character = null;
    }

    @Test
    public void giveInput() throws Exception {
        Vector2f movementVector = new Vector2f();
        Vector2f attackVector = new Vector2f(1f, 1f);
        character.inventoryChanged();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                movementVector.set((float) (Math.random() * i), (float) (Math.random() * j));
                if (movementVector.x == 0 && movementVector.y == 0) {
                    continue;
                }
                movementVector.normalize();
                character.giveInput(movementVector, attackVector);
                assertTrue((Math.abs(fakePhysicsActor.getPosition().x - movementVector.x) < 0.01f));
                assertTrue(Math.abs(fakePhysicsActor.getPosition().y - movementVector.y) < 0.01);
            }
        }
    }

    @Test
    public void takeDamage() throws Exception {
        character.inventoryChanged();
        int startHealth = character.currentHealth;
        for (int i = 1; i < 100; i++) {
            character.takeDamage(i);

            assertEquals(startHealth - i, character.currentHealth);
            character.currentHealth = startHealth;
        }
    }

    @Test
    public void stats() throws Exception {
        final int BASE_HEALTH = 100;
        Inventory inventory = setUpInventory();

        Item item = new Item() {
            @Override
            protected void initDynamic() {
                statMap.put(Stat.HEALTH, -50.0);
                statMap.put(Stat.ACCEL, 100.0);
                setX(1);
                setY(0);
                basePositions.add(new Point2i(0, 0));
            }
        };


        assertEquals(BASE_HEALTH, character.currentHealth);

        inventory.addItem(item);

        assertEquals(Math.round(inventory.getStatSum(Stat.HEALTH)) + BASE_HEALTH, character.currentHealth);
    }

    private Inventory setUpInventory() {
        Inventory inventory = new Inventory();
        inventory.setDimensions(10, 10);

        Item item = new Item() {
            @Override
            protected void initDynamic() {
                statMap.put(Stat.HEALTH, 100.0);
                setX(0);
                setY(0);
                basePositions.add(new Point2i(0, 0));
            }
        };

        inventory.addItem(item);

        character.setInventory(inventory);
        inventory.addInventoryChangeListener(character);

        return inventory;
    }
}