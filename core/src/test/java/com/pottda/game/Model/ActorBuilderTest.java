package com.pottda.game.Model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Magnus on 2017-04-25.
 */
public class ActorBuilderTest {
    private Actor actorCharacter;
    private Actor actorProjectile;
    private Actor actorObstacle;
    private Character character;
    private Projectile projectile;
    private Obstacle obstacle;

    @Before
    public void setUp() throws Exception {
        actorCharacter = ActorBuilder.getCharacter();
        actorProjectile = ActorBuilder.getProjectile();
        actorObstacle = ActorBuilder.getObstacle();

        character = ActorBuilder.getCharacter();
        projectile = ActorBuilder.getProjectile();
        obstacle = ActorBuilder.getObstacle();

        ActorBuilder.deactivateActor(actorCharacter);
        ActorBuilder.deactivateActor(actorProjectile);
        ActorBuilder.deactivateActor(actorObstacle);
        ActorBuilder.deactivateActor(character);
        ActorBuilder.deactivateActor(projectile);
        ActorBuilder.deactivateActor(obstacle);
    }

    @Test
    public void getCharacter() throws Exception {
        Actor c1 = ActorBuilder.getCharacter();
        Actor c2 = ActorBuilder.getCharacter();
        Actor c3 = ActorBuilder.getCharacter();

        Assert.assertEquals(c1, character);
        Assert.assertEquals(c2, actorCharacter);
        Assert.assertNotEquals(c3, character);
        Assert.assertNotEquals(c3, actorCharacter);
    }

    @Test
    public void getProjectile() throws Exception {
        Actor p1 = ActorBuilder.getProjectile();
        Actor p2 = ActorBuilder.getProjectile();
        Actor p3 = ActorBuilder.getProjectile();

        Assert.assertEquals(p1, projectile);
        Assert.assertEquals(p2, actorProjectile);
        Assert.assertNotEquals(p3, projectile);
        Assert.assertNotEquals(p3, actorProjectile);
    }

    @Test
    public void getObstacle() throws Exception {
        Actor o1 = ActorBuilder.getObstacle();
        Actor o2 = ActorBuilder.getObstacle();
        Actor o3 = ActorBuilder.getObstacle();

        Assert.assertEquals(o1, obstacle);
        Assert.assertEquals(o2, actorObstacle);
        Assert.assertNotEquals(o3, obstacle);
        Assert.assertNotEquals(o3, actorObstacle);
    }

    @Test
    public void deactivateActor() throws Exception {
    }


}
