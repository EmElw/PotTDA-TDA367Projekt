package com.pottda.game.model.builders;

import com.pottda.game.model.Character;
import com.pottda.game.model.*;
import org.junit.Before;
import org.junit.Test;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.List;

import static com.pottda.game.assets.Sprites.BORDER;
import static com.pottda.game.assets.Sprites.ENEMYPROJECTILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Magnus on 2017-05-18.
 */
public class BuilderTest {

    private DummyListener myDummyListener;

    @Before
    public void setUp() {
        // Set concrete PhysicsActorFactory implementation
        AbstractModelBuilder.setPhysiscActorFactory(new PhysicsActorFactory() {

            private final PhysicsActor pa = new PhysicsActor() {

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

        // Add listeners
        AbstractModelBuilder.addListener(myDummyListener = new DummyListener());

    }

    @Test
    public void obstacleTest() {
        ModelActor obstacle = new ObstacleBuilder().setSprite(BORDER).create();

        assertEquals(BORDER, obstacle.getSprite());

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
                setSprite(ENEMYPROJECTILE).
                create();

        // Create a projectile that is mostly default parameters
        Projectile projB = (Projectile) new ProjectileBuilder().
                setSprite(ENEMYPROJECTILE).
                create();

        // Test the fiddled projectile
        assertEquals(true, projA.isBouncy());
        assertEquals(true, projA.isPiercing());
        assertEquals(10, projA.damage);
        assertEquals(50, projA.getLifeTimeMS());
        assertEquals(ENEMYPROJECTILE, projA.getSprite());
        assertEquals(0, projA.getListeners().size());

        // Test the mostly default projectile
        assertEquals(0, projB.damage);
        assertEquals(ENEMYPROJECTILE, projB.getSprite());
        assertEquals(0, projB.getListeners().size());


        assertEquals(2, myDummyListener.calls);
        assertTrue(myDummyListener.modelActors.contains(projA));
        assertTrue(myDummyListener.modelActors.contains(projB));
    }


    private class DummyListener implements NewModelListener {

        int calls = 0;

        private final List<ModelActor> modelActors = new ArrayList<ModelActor>();

        @Override
        public void onNewModel(ModelActor m) {
            calls++;
            modelActors.add(m);
        }
    }

}
