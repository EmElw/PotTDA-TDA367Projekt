package com.pottda.game.actorFactory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pottda.game.controller.ControllerOptions;
import com.pottda.game.controller.*;
import com.pottda.game.model.*;
import com.pottda.game.model.Character;
import com.pottda.game.physicsBox2D.Box2DPhysicsActor;
import com.pottda.game.physicsBox2D.Box2DPhysicsCharacter;
import com.pottda.game.physicsBox2D.Box2DPhysicsProjectile;
import com.pottda.game.view.ViewActor;

import javax.vecmath.Vector2f;

public class Box2DActorFactory extends ActorFactory {
    // Constants
    private final static float GRAVITY = 0f;

    public final static int PLAYER_TEAM = 0;
    public final static int ENEMY_TEAM = 1;

    private final static float CHARACTER_LINEAR_DAMPING = 0f;
    private final static float CHARACTER_ANGULAR_DAMPING = 0.1f;
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
    private static Filter PROJECTILE_PENETRATION_FILTER;

    private final static float PROJECTILE_SENSOR_RADIUS = 0.05f;
    private final static float PROJECTILE_SENSOR_DENSITY = 0f;

    private final static float OBSTACLE_HALF_WIDTH = 0.5f;
    private final static float OBSTACLE_HALF_HEIGHT = 0.5f;
    private final static float OBSTACLE_FRICTION = 0.25f;
    private final static float OBSTACLE_BOUNCINESS = 0f;


    private final World world;

    private BodyDef characterBodyDef;
    private BodyDef projectileBodyDef;
    private BodyDef obstacleBodyDef;

    private FixtureDef characterFixtureDef;
    private FixtureDef projectileFixtureDef;
    private FixtureDef projectileSensorFixtureDef;
    private FixtureDef obstacleFixtureDef;

    public Box2DActorFactory(World world) {
        this.world = world;
        filterCategoryInit();
        bodyDefInit();
        fixtureDefInit();
    }

    @Override
    public AIController buildEnemy(Stage stage, Texture texture, Vector2f position) {
        // Create body
        Body body = world.createBody(characterBodyDef);
        body.setTransform(position.getX(), position.getY(), 0);

        // Set collision filtering
        characterFixtureDef.filter.categoryBits = CHARACTER_ENEMY_FILTER.categoryBits;
        characterFixtureDef.filter.maskBits = CHARACTER_ENEMY_FILTER.maskBits;

        // Give body a fixture and calculate mass
        body.createFixture(characterFixtureDef);
        body.resetMassData();

        Box2DPhysicsCharacter physics = new Box2DPhysicsCharacter(body);

        Character model = new Character(physics);
        model.team = ENEMY_TEAM;

        ViewActor view = new ViewActor(texture);

        DumbAIController controller = new DumbAIController(model, view, stage);

        return controller;
    }

    /**
     * Creates a player controller
     *
     * @param stage    the stage to render the player on
     * @param texture  a texture/image for the player
     * @param position the position where the player is created
     * @return
     */
    @Override
    public AbstractController buildPlayer(Stage stage, Texture texture, Vector2f position) {
        // Create body
        Body body = world.createBody(characterBodyDef);
        body.setTransform(position.getX(), position.getY(), 0);

        // Set collision filtering
        characterFixtureDef.filter.categoryBits = CHARACTER_PLAYER_FILTER.categoryBits;
        characterFixtureDef.filter.maskBits = CHARACTER_PLAYER_FILTER.maskBits;

        // Give body a fixture and calculate mass
        body.createFixture(characterFixtureDef);
        body.resetMassData();

        Box2DPhysicsCharacter physics = new Box2DPhysicsCharacter(body);

        Character model = new Character(physics);
        DumbAIController.goal = model;
        model.team = PLAYER_TEAM;

        ViewActor view = new ViewActor(texture);

        AbstractController controller = null;

        switch (ControllerOptions.controllerSettings) {
            case ControllerOptions.TOUCH_JOYSTICK:
                controller = new TouchJoystickController(model, view, ControllerOptions.joystickStage);
                break;
            case ControllerOptions.KEYBOARD_MOUSE:
                controller = new KeyboardMouseController(model, view, stage);
                break;
            case ControllerOptions.KEYBOARD_ONLY:
                controller = new KeyboardOnlyController(model, view, stage);
                break;
        }

        return controller;
    }

    @Override
    public ProjectileController buildProjectile(Stage stage, Texture texture, int team, boolean bounces, boolean penetrates, Vector2f position) {
        // Create body
        Body body = world.createBody(projectileBodyDef);
        body.setTransform(position.getX(), position.getY(), 0);

        // Determine bounciness
        if (bounces) {
            projectileFixtureDef.restitution = PROJECTILE_BOUNCINESS_BOUNCY;
        } else {
            projectileFixtureDef.restitution = PROJECTILE_BOUNCINESS_DEFAULT;
        }

        // Determine collision filtering
        if (penetrates) {
            projectileFixtureDef.filter.categoryBits = PROJECTILE_PENETRATION_FILTER.categoryBits;
            projectileFixtureDef.filter.maskBits = PROJECTILE_PENETRATION_FILTER.maskBits;
        } else if (team == ENEMY_TEAM) {
            projectileFixtureDef.filter.categoryBits = PROJECTILE_ENEMY_FILTER.categoryBits;
            projectileFixtureDef.filter.maskBits = PROJECTILE_ENEMY_FILTER.maskBits;
        } else {
            projectileFixtureDef.filter.categoryBits = PROJECTILE_PLAYER_FILTER.categoryBits;
            projectileFixtureDef.filter.maskBits = PROJECTILE_PLAYER_FILTER.maskBits;
        }

        // Give body fixtures and calculate mass
        body.createFixture(projectileFixtureDef);
        body.createFixture(projectileSensorFixtureDef);
        body.resetMassData();

        Box2DPhysicsProjectile physics = new Box2DPhysicsProjectile(body);

        Projectile model = new Projectile(physics, 0, null);
        model.team = team;

        ViewActor view = new ViewActor(texture);

        ProjectileController controller = new ProjectileController(model, view, stage);

        return controller;
    }

    @Override
    public AbstractController buildObstacle(Stage stage, Texture texture, Vector2f position) {
        Body body = world.createBody(obstacleBodyDef);
        body.setTransform(position.getX(), position.getY(), 0);

        body.createFixture(obstacleFixtureDef);

        Box2DPhysicsActor physics = new Box2DPhysicsActor(body);

        Obstacle model = new Obstacle(physics);

        ViewActor view = new ViewActor(texture);

        ObstacleController controller = new ObstacleController(model, view, stage);

        return controller;
    }

    private void filterCategoryInit() {
        CHARACTER_PLAYER_FILTER = new Filter();
        CHARACTER_ENEMY_FILTER = new Filter();
        PROJECTILE_PLAYER_FILTER = new Filter();
        PROJECTILE_ENEMY_FILTER = new Filter();
        PROJECTILE_PENETRATION_FILTER = new Filter();

        CHARACTER_PLAYER_FILTER.categoryBits = 0x0001;
        CHARACTER_ENEMY_FILTER.categoryBits = 0x0002;
        PROJECTILE_PLAYER_FILTER.categoryBits = 0x0004;
        PROJECTILE_ENEMY_FILTER.categoryBits = 0x0008;
        PROJECTILE_PENETRATION_FILTER.categoryBits = 0x0010;

        // Player collides with Enemy Character and Projectile
        CHARACTER_PLAYER_FILTER.maskBits = (short) ((int) CHARACTER_ENEMY_FILTER.categoryBits +
                (int) PROJECTILE_ENEMY_FILTER.categoryBits);

        // Enemy collides with itself, Player Character and Projectile
        CHARACTER_ENEMY_FILTER.maskBits = (short) ((int) CHARACTER_PLAYER_FILTER.categoryBits +
                (int) CHARACTER_ENEMY_FILTER.categoryBits + (int) PROJECTILE_PLAYER_FILTER.categoryBits);

        // Player Projectile collides with Enemy Character
        PROJECTILE_PLAYER_FILTER.maskBits = CHARACTER_ENEMY_FILTER.categoryBits;

        // Enemy Projectile collides with Player Character

        PROJECTILE_ENEMY_FILTER.maskBits = CHARACTER_PLAYER_FILTER.categoryBits;

        // Projectiles with the 'penetrates' flag only collide with the terrain
        PROJECTILE_PENETRATION_FILTER.maskBits = 0x0000;
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
        obstacleFixtureDef = new FixtureDef();

        PolygonShape tempPolygon;
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
        //projectileSensorFixtureDef.shape = tempCircle;
        //projectileSensorFixtureDef.density = PROJECTILE_SENSOR_DENSITY;
        //projectileSensorFixtureDef.isSensor = true;

        // Obstacle
        tempPolygon = new PolygonShape();
        tempPolygon.setAsBox(OBSTACLE_HALF_WIDTH, OBSTACLE_HALF_HEIGHT);
        obstacleFixtureDef.shape = tempPolygon;
        obstacleFixtureDef.friction = OBSTACLE_FRICTION;
        obstacleFixtureDef.restitution = OBSTACLE_BOUNCINESS;
    }
}
