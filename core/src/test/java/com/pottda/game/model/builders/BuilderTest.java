package com.pottda.game.model.builders;

import com.pottda.game.model.*;
import com.pottda.game.model.Character;
import com.pottda.game.view.Sprites;
import org.junit.Before;
import org.junit.Test;

import javax.vecmath.Tuple2f;
import javax.vecmath.Vector2f;

import static com.pottda.game.view.Sprites.*;
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
            @Override
            public PhysicsActor getProjectilePhysicsActor(Projectile projectile) {
                return null;
            }

            @Override
            public PhysicsActor getCharacterPhysicsActor(Character character) {
                return null;
            }

            @Override
            public PhysicsActor getObstaclePhysicsActor(Obstacle obstacle, Tuple2f dimensions) {

                return null;
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
        Projectile projA = (Projectile) new ProjectileBuilder().
                setBouncy().
                setPiercing().
                setDamage(10).
                setLifetime(50).
                setSprite(PROJECTILE1).
                create();

        Projectile projB = (Projectile) new ProjectileBuilder().
                setDamage(20).
                setSprite(PROJECTILE1).
                create();

        assertEquals(true, projA.isBouncy);
        assertEquals(true, projA.isPiercing);
        assertEquals(10, projA.damage);
        assertEquals(50, projA.lifeTimeMS);
        assertEquals(PROJECTILE1, projA.sprite);

        assertEquals(20, projB.damage);
        assertEquals(PROJECTILE1, projB.sprite);

        assertEquals(2, myDummyListener.calls);

    }


    private class DummyListener implements NewModelListener {

        int calls = 0;

        @Override
        public void onNewModel(ModelActor m) {
            calls++;
        }
    }

}
