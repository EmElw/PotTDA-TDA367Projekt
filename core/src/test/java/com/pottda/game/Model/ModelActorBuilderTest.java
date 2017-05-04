package com.pottda.game.Model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Magnus on 2017-04-25.
 */
public class ModelActorBuilderTest {
    private ModelActor modelActorCharacter;
    private ModelActor modelActorProjectile;
    private ModelActor modelActorObstacle;
    private Character character;
    private Projectile projectile;
    private Obstacle obstacle;

    @Before
    public void setUp() throws Exception {
        modelActorCharacter = ActorBuilder.getCharacter();
        modelActorProjectile = ActorBuilder.getProjectile();
        modelActorObstacle = ActorBuilder.getObstacle();

        character = ActorBuilder.getCharacter();
        projectile = ActorBuilder.getProjectile();
        obstacle = ActorBuilder.getObstacle();

        ActorBuilder.deactivateActor(modelActorCharacter);
        ActorBuilder.deactivateActor(modelActorProjectile);
        ActorBuilder.deactivateActor(modelActorObstacle);
        ActorBuilder.deactivateActor(character);
        ActorBuilder.deactivateActor(projectile);
        ActorBuilder.deactivateActor(obstacle);
    }

    @Test
    public void getCharacter() throws Exception {
        ModelActor c1 = ActorBuilder.getCharacter();
        ModelActor c2 = ActorBuilder.getCharacter();
        ModelActor c3 = ActorBuilder.getCharacter();

        Assert.assertEquals(c1, character);
        Assert.assertEquals(c2, modelActorCharacter);
        Assert.assertNotEquals(c3, character);
        Assert.assertNotEquals(c3, modelActorCharacter);
    }

    @Test
    public void getProjectile() throws Exception {
        ModelActor p1 = ActorBuilder.getProjectile();
        ModelActor p2 = ActorBuilder.getProjectile();
        ModelActor p3 = ActorBuilder.getProjectile();

        Assert.assertEquals(p1, projectile);
        Assert.assertEquals(p2, modelActorProjectile);
        Assert.assertNotEquals(p3, projectile);
        Assert.assertNotEquals(p3, modelActorProjectile);
    }

    @Test
    public void getObstacle() throws Exception {
        ModelActor o1 = ActorBuilder.getObstacle();
        ModelActor o2 = ActorBuilder.getObstacle();
        ModelActor o3 = ActorBuilder.getObstacle();

        Assert.assertEquals(o1, obstacle);
        Assert.assertEquals(o2, modelActorObstacle);
        Assert.assertNotEquals(o3, obstacle);
        Assert.assertNotEquals(o3, modelActorObstacle);
    }

    @Test
    public void deactivateActor() throws Exception {
    }


}
