package com.pottda.game.model.builders;

import com.pottda.game.model.*;
import com.pottda.game.model.Character;
import org.junit.Before;
import org.junit.Test;

import javax.vecmath.Tuple2f;
import javax.vecmath.Vector2f;

import java.util.ArrayList;
import java.util.List;

import static com.pottda.game.model.Sprites.*;
import static org.junit.Assert.*;

/**
 * Created by Magnus on 2017-05-18.
 */
public class BuilderTest {

    DummyListener myDummyListener;

    @Before
    public void setUp() {
        // Set concrete PhysicsActorFactory implementation
        AbstractModelBuilder.setPhysiscActorFactory(new PhysicsActorFactory() {

            private PhysicsActor pa = new PhysicsActor() {

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
            public PhysicsActor getObstaclePhysicsActor(Obstacle obstacle, Tuple2f dimensions) {
                return pa;
            }
        });

        // Add listeners
        AbstractModelBuilder.addListener(myDummyListener = new DummyListener());

    }

    @Test
    public void obstacleTest() {
        ModelActor obstacle = new ObstacleBuilder().setSprite(BORDER).create();

        assertEquals(BORDER, obstacle.sprite);

        assertEquals(1, myDummyListener.calls);
    }

    @Test
    public void projectileTest() {
        // Create a projectile and fiddle with most parameters
        Projectile projA = (Projectile) new ProjectileBuilder().
                setBouncy().
                setPiercing().
                setDamage(10).
                setLifetime(50).
                setSprite(PROJECTILE1).
                create();

        // Create a projectile that is mostly default parameters
        Projectile projB = (Projectile) new ProjectileBuilder().
                setSprite(PROJECTILE1).
                create();

        // Test the fiddled projectile
        assertEquals(true, projA.isBouncy);
        assertEquals(true, projA.isPiercing);
        assertEquals(10, projA.damage);
        assertEquals(50, projA.lifeTimeMS);
        assertEquals(PROJECTILE1, projA.sprite);
        assertEquals(0, projA.getListeners().size());

        // Test the mostly default projectile
        assertEquals(0, projB.damage);
        assertEquals(PROJECTILE1, projB.sprite);
        assertEquals(0, projB.getListeners().size());


        assertEquals(2, myDummyListener.calls);
        assertTrue(myDummyListener.modelActors.contains(projA));
        assertTrue(myDummyListener.modelActors.contains(projB));
    }


    private class DummyListener implements NewModelListener {

        int calls = 0;

        private List<ModelActor> modelActors = new ArrayList<ModelActor>();

        @Override
        public void onNewModel(ModelActor m) {
            calls++;
            modelActors.add(m);
        }
    }

}
