package com.pottda.game.model;

import com.pottda.game.model.builders.AbstractModelBuilder;
import com.pottda.game.model.builders.CharacterBuilder;
import com.pottda.game.model.builders.ProjectileBuilder;
import org.junit.Before;
import org.junit.Test;

import javax.vecmath.Vector2f;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProjectileTest {
    private final PhysicsActorFactory paf = new PhysicsActorFactory() {
        final PhysicsActor pa = new PhysicsActor() {
            Vector2f position = new Vector2f();

            @Override
            public Vector2f getPosition() {
                return position;
            }

            @Override
            public void giveMovementVector(Vector2f movementVector) {
                position.add(movementVector);
            }

            @Override
            public void destroyBody() {

            }

            @Override
            public void setPosition(Vector2f position) {
                this.position = position;
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
    };
    private Projectile projectile;

    @Before
    public void setUp() throws Exception {
        AbstractModelBuilder.setPhysiscActorFactory(paf);

        projectile = (Projectile) new ProjectileBuilder().
                setTeam(Character.PLAYER_TEAM).
                setPosition(new Vector2f()).
                setSprite(com.pottda.game.assets.Sprites.ENEMYPROJECTILE).
                create();
    }

    @Test
    public void giveInput() throws Exception {
        Vector2f currentPosition = new Vector2f(projectile.getPosition().x, projectile.getPosition().y);
        Vector2f movementVector = new Vector2f();

        // Check if giveInput works in the eight main directions
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                if (x != 0 || y != 0) {
                    movementVector.set(x, y);
                    movementVector.normalize();

                    projectile.giveInput(movementVector, null);
                    currentPosition.add(movementVector);

                    assertEquals(currentPosition.x, projectile.getPosition().x, 0.01);
                    assertEquals(currentPosition.y, projectile.getPosition().y, 0.01);
                }
            }
        }
    }

    @Test
    public void getAngle() throws Exception {
        Vector2f movementVector = new Vector2f();

        // Check if getAngle works for movements in the eight main directions
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                if (x != 0 || y != 0) {
                    movementVector.set(x, y);
                    movementVector.normalize();

                    projectile.giveInput(movementVector, null);

                    assertEquals(movementVector.angle(new Vector2f(1, 0)) * ((y < 0) ? -1 : 1), Math.toRadians(projectile.getAngle()), 0.01);
                }
            }
        }
    }

    @Test
    public void onCollision() throws Exception {
        Character character = (Character) new CharacterBuilder().
                setTeam(Character.ENEMY_TEAM).
                setInventoryFromFile("testInv.xml").
                setPosition(new Vector2f()).
                setSprite(com.pottda.game.assets.Sprites.ENEMY).
                create();

        int projectileDamage = 10;
        projectile.damage = projectileDamage;

        int characterMaxHP = character.getCurrentHealth();

        projectile.onCollision(character);

        assertEquals(characterMaxHP - projectileDamage, character.getCurrentHealth());
        assertTrue(projectile.shouldBeRemoved);
    }

    @Test
    public void onCollision1() throws Exception {
        projectile.onCollision();

        assertTrue(projectile.shouldBeRemoved);
    }

    @Test
    public void isDying() throws Exception {
        projectile.lifeTimeMS = (long) 100;
        projectile.update(0.11f);
        projectile.giveInput(new Vector2f(), new Vector2f());

        assertTrue(projectile.shouldBeRemoved);
    }
}