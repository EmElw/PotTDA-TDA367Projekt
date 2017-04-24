package com.pottda.game.PhysicsWorldAdapter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.pottda.game.Model.Actor;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.vecmath.Vector2f;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Gustav Lahti on 2017-04-21.
 */
public class Box2DAdapterTest {
    Random random;
    PhysicsWorldAdapter adapter;
    World world;
    Actor actor;
    Body body;
    BodyDef bodyDef;
    Vector2f vector = new Vector2f(0f, 0f);

    @BeforeClass
    public void init() throws Exception{
        random = new Random();

        adapter = new Box2DAdapter();
        world = new World(new Vector2(0f, 0f), true);

        actor = new Actor() {
            @Override
            public void collide(Actor other) {}
        };
        bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0f, 0f);
        bodyDef.linearDamping = 0f;
        bodyDef.angularDamping = 0f;
        bodyDef.gravityScale = 0f;
        bodyDef.allowSleep = false;
        bodyDef.awake = true;
    }

    @Before
    public void setUp() throws Exception {
        body = world.createBody(bodyDef);
    }

    @After
    public void tearDown() throws Exception{
        adapter.actors.clear();
        adapter.bodies.clear();
        world.destroyBody(body);
    }

    @Test
    public void applyForceToCenter() throws Exception {
        adapter.actors.add(actor);
        adapter.bodies.add(body);

        vector.set(random.nextFloat() * (random.nextInt(10) + 1), random.nextFloat() * (random.nextInt(10) + 1));

        adapter.applyForceToCenter(actor, vector);

        Vector2 tempVector = adapter.bodies.get(0).getLinearVelocity();

        assertTrue(tempVector.x / tempVector.y == vector.x / vector.y);
    }

    @Test
    public void setLinearVelocity() throws Exception {
    }

}