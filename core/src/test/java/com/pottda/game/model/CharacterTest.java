package com.pottda.game.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.vecmath.Vector2f;
import javax.xml.ws.soap.Addressing;

import java.util.Random;

import static org.junit.Assert.*;

public class CharacterTest {
    private Character character;
    private PhysicsActor fakePhysicsActor;

    @Before
    public void setUp() throws Exception {
        fakePhysicsActor = new PhysicsActor() {
            public Vector2f position;

            @Override
            public Vector2f getPosition() {
                return position;
            }

            @Override
            public void giveMovementVector(Vector2f movementVector) {
                position = movementVector;
            }
        };
        character = new Character(fakePhysicsActor);
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
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                movementVector.set((float) (15 * i), (float) (37 * j));
                character.giveInput(movementVector, attackVector);
                assertTrue((Math.abs(fakePhysicsActor.getPosition().x - movementVector.x) < 0.01f) &&
                        (Math.abs(fakePhysicsActor.getPosition().y - movementVector.y) < 0.01));
            }
        }
    }

    @Test
    public void takeDamage() throws Exception {
        int startHealth = character.currentHealth;
        int damage = new Random().nextInt(startHealth / 2);
        character.takeDamage(damage);

        assertEquals(character.currentHealth, startHealth - damage);
    }

}