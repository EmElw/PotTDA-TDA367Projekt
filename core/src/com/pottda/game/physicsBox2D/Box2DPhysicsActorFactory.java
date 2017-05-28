package com.pottda.game.physicsBox2D;

import com.badlogic.gdx.physics.box2d.*;
import com.pottda.game.model.Character;
import com.pottda.game.model.*;

import static com.pottda.game.model.ModelActor.ENEMY_TEAM;
import static com.pottda.game.model.ModelActor.PLAYER_TEAM;

public class Box2DPhysicsActorFactory implements PhysicsActorFactory{
    private final static float GRAVITY = 0f;

    private final static float CHARACTER_LINEAR_DAMPING = 4f;
    private final static float CHARACTER_ANGULAR_DAMPING = 4f;
    private final static float CHARACTER_RADIUS = 0.5f;
    private final static float CHARACTER_DENSITY = 1f;
    private final static float CHARACTER_FRICTION = 0.25f;
    private final static float CHARACTER_BOUNCINESS = 0.1f;
    private static Filter CHARACTER_PLAYER_FILTER;
    private static Filter CHARACTER_ENEMY_FILTER;

    private final static float PROJECTILE_DAMPING = 0f;
    private final static float PROJECTILE_RADIUS = 0.04f;
    private final static float PROJECTILE_DENSITY = 1f;
    private final static float PROJECTILE_FRICTION = 1f;
    private final static float PROJECTILE_BOUNCINESS_DEFAULT = 0f;
    private final static float PROJECTILE_BOUNCINESS_BOUNCY = 1f;
    private static Filter PROJECTILE_PLAYER_FILTER;
    private static Filter PROJECTILE_ENEMY_FILTER;
    private static Filter PROJECTILE_PIERCING_FILTER;

    private final static float PROJECTILE_SENSOR_RADIUS = 0.05f;
    private final static float PROJECTILE_SENSOR_DENSITY = 0f;

    private final static float OBSTACLE_FRICTION = 0.25f;
    private final static float OBSTACLE_BOUNCINESS = 0f;
    private static Filter OBSTACLE_FILTER;

    private final World world;

    private BodyDef characterBodyDef;
    private BodyDef projectileBodyDef;
    private BodyDef obstacleBodyDef;

    private FixtureDef characterFixtureDef;
    private FixtureDef projectileFixtureDef;
    private FixtureDef projectileSensorFixtureDef;
    private FixtureDef obstacleFixtureDef;

    public Box2DPhysicsActorFactory(World world){
        this.world = world;
        filterCategoryInit();
        bodyDefInit();
        fixtureDefInit();
    }

    @Override
    public PhysicsActor getProjectilePhysicsActor(Projectile projectile) {
        if (projectile.isBouncy()) {
            projectileFixtureDef.restitution = PROJECTILE_BOUNCINESS_BOUNCY;
        } else {
            projectileFixtureDef.restitution = PROJECTILE_BOUNCINESS_DEFAULT;
        }

        // Determine collision filtering
        if (projectile.isPiercing()) {
            projectileFixtureDef.filter.categoryBits = PROJECTILE_PIERCING_FILTER.categoryBits;
            projectileFixtureDef.filter.maskBits = PROJECTILE_PIERCING_FILTER.maskBits;
        } else if (projectile.getTeam() == ENEMY_TEAM) {
            projectileFixtureDef.filter.categoryBits = PROJECTILE_ENEMY_FILTER.categoryBits;
            projectileFixtureDef.filter.maskBits = PROJECTILE_ENEMY_FILTER.maskBits;
        } else {
            projectileFixtureDef.filter.categoryBits = PROJECTILE_PLAYER_FILTER.categoryBits;
            projectileFixtureDef.filter.maskBits = PROJECTILE_PLAYER_FILTER.maskBits;
        }

        Body body = world.createBody(projectileBodyDef);
        body.createFixture(projectileFixtureDef);
        body.createFixture(projectileSensorFixtureDef);

        // Set body's user data, used in collision handling
        body.setUserData(projectile);

        return new Box2DPhysicsProjectile(body);
    }

    @Override
    public PhysicsActor getCharacterPhysicsActor(Character character) {
        // Set collision filters for character
        switch (character.getTeam()){
            case PLAYER_TEAM:
                characterFixtureDef.filter.categoryBits = CHARACTER_PLAYER_FILTER.categoryBits;
                characterFixtureDef.filter.maskBits = CHARACTER_PLAYER_FILTER.maskBits;
                break;
            case ENEMY_TEAM:
                characterFixtureDef.filter.categoryBits = CHARACTER_ENEMY_FILTER.categoryBits;
                characterFixtureDef.filter.maskBits = CHARACTER_ENEMY_FILTER.maskBits;
                break;
        }

        Body body = world.createBody(characterBodyDef);
        body.createFixture(characterFixtureDef);

        // Set body's user data, used in collision handling
        body.setUserData(character);
        
        return new Box2DPhysicsCharacter(body);
    }

    @Override
    public PhysicsActor getObstaclePhysicsActor(Obstacle obstacle) {
        Shape tempShape;
        if(obstacle.getRound()){
            tempShape = new CircleShape();
            ((CircleShape)tempShape).setRadius(obstacle.getSize().getX());
        } else {
            tempShape = new PolygonShape();
            ((PolygonShape)tempShape).setAsBox(obstacle.getSize().x / 2, obstacle.getSize().y / 2);
        }
        obstacleFixtureDef.shape = tempShape;

        Body body = world.createBody(obstacleBodyDef);
        body.createFixture(obstacleFixtureDef);

        // Set body's user data, used in collision handling
        body.setUserData(obstacle);
        
        return new Box2DPhysicsActor(body);
    }

    private void filterCategoryInit() {
        CHARACTER_PLAYER_FILTER = new Filter();
        CHARACTER_ENEMY_FILTER = new Filter();
        PROJECTILE_PLAYER_FILTER = new Filter();
        PROJECTILE_ENEMY_FILTER = new Filter();
        PROJECTILE_PIERCING_FILTER = new Filter();
        OBSTACLE_FILTER = new Filter();

        CHARACTER_PLAYER_FILTER.categoryBits = 0x0001;
        CHARACTER_ENEMY_FILTER.categoryBits = 0x0002;
        PROJECTILE_PLAYER_FILTER.categoryBits = 0x0004;
        PROJECTILE_ENEMY_FILTER.categoryBits = 0x0008;
        PROJECTILE_PIERCING_FILTER.categoryBits = 0x0010;
        OBSTACLE_FILTER.categoryBits = 0x0020;

        // Player collides with Enemy Character and Projectile, and Obstacle
        CHARACTER_PLAYER_FILTER.maskBits = (short) (CHARACTER_PLAYER_FILTER.categoryBits |
                CHARACTER_ENEMY_FILTER.categoryBits |
                PROJECTILE_ENEMY_FILTER.categoryBits |
                OBSTACLE_FILTER.categoryBits);

        // Enemy collides with itself, Player Character and Projectile, and Obstacle
        CHARACTER_ENEMY_FILTER.maskBits = (short)(CHARACTER_PLAYER_FILTER.categoryBits |
                CHARACTER_ENEMY_FILTER.categoryBits |
                PROJECTILE_PLAYER_FILTER.categoryBits |
                OBSTACLE_FILTER.categoryBits);

        // Player Projectile collides with Enemy Character and Obstacle
        PROJECTILE_PLAYER_FILTER.maskBits = (short) (CHARACTER_ENEMY_FILTER.categoryBits |
                OBSTACLE_FILTER.categoryBits);

        // Enemy Projectile collides with Player Character and Obstacle
        PROJECTILE_ENEMY_FILTER.maskBits = (short) (CHARACTER_PLAYER_FILTER.categoryBits |
                OBSTACLE_FILTER.categoryBits);

        // Projectiles with the 'piercing' flag only collide with Obstacle
        PROJECTILE_PIERCING_FILTER.maskBits = OBSTACLE_FILTER.categoryBits;

        // Obstacles collide with everything
        OBSTACLE_FILTER.maskBits = (short) (CHARACTER_PLAYER_FILTER.categoryBits |
                CHARACTER_ENEMY_FILTER.categoryBits |
                PROJECTILE_PLAYER_FILTER.categoryBits |
                PROJECTILE_ENEMY_FILTER.categoryBits |
                PROJECTILE_PIERCING_FILTER.categoryBits);
    }

    private void bodyDefInit() {
        characterBodyDef = new BodyDef();
        projectileBodyDef = new BodyDef();
        obstacleBodyDef = new BodyDef();

        // Character (enemy and player)
        characterBodyDef.type = BodyDef.BodyType.DynamicBody;
        characterBodyDef.linearDamping = CHARACTER_LINEAR_DAMPING;
        characterBodyDef.angularDamping = CHARACTER_ANGULAR_DAMPING;
        characterBodyDef.gravityScale = GRAVITY;
        characterBodyDef.fixedRotation = true;

        // Projectile
        projectileBodyDef.type = BodyDef.BodyType.DynamicBody;
        projectileBodyDef.linearDamping = PROJECTILE_DAMPING;
        projectileBodyDef.angularDamping = PROJECTILE_DAMPING;
        projectileBodyDef.gravityScale = GRAVITY;
        projectileBodyDef.fixedRotation = true;

        // Obstacle
        obstacleBodyDef.type = BodyDef.BodyType.StaticBody;
    }

    private void fixtureDefInit() {
        characterFixtureDef = new FixtureDef();
        projectileFixtureDef = new FixtureDef();
        projectileSensorFixtureDef = new FixtureDef();
        obstacleFixtureDef = new FixtureDef();

        CircleShape tempCircle;

        // Character (enemy and player)
        tempCircle = new CircleShape();
        tempCircle.setRadius(CHARACTER_RADIUS);
        characterFixtureDef.shape = tempCircle;
        characterFixtureDef.density = CHARACTER_DENSITY;
        characterFixtureDef.friction = CHARACTER_FRICTION;
        characterFixtureDef.restitution = CHARACTER_BOUNCINESS;

        // Projectile, collision part
        tempCircle = new CircleShape();
        tempCircle.setRadius(PROJECTILE_RADIUS);
        projectileFixtureDef.shape = tempCircle;
        projectileFixtureDef.density = PROJECTILE_DENSITY;
        projectileFixtureDef.friction = PROJECTILE_FRICTION;
        projectileFixtureDef.restitution = PROJECTILE_BOUNCINESS_DEFAULT;

        // Projectile, sensor part
        tempCircle = new CircleShape();
        tempCircle.setRadius(PROJECTILE_SENSOR_RADIUS);
        projectileSensorFixtureDef.shape = tempCircle;
        projectileSensorFixtureDef.density = PROJECTILE_SENSOR_DENSITY;
        projectileSensorFixtureDef.isSensor = true;

        // Obstacle
        obstacleFixtureDef.friction = OBSTACLE_FRICTION;
        obstacleFixtureDef.restitution = OBSTACLE_BOUNCINESS;
        obstacleFixtureDef.filter.categoryBits = OBSTACLE_FILTER.categoryBits;
        obstacleFixtureDef.filter.maskBits = OBSTACLE_FILTER.maskBits;
    }
}
