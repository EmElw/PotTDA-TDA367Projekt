package com.pottda.game.actorFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pottda.game.view.Sprites;
import com.pottda.game.controller.ControllerOptions;
import com.pottda.game.controller.*;
import com.pottda.game.model.*;
import com.pottda.game.model.Character;
import com.pottda.game.physicsBox2D.Box2DPhysicsActor;
import com.pottda.game.physicsBox2D.Box2DPhysicsCharacter;
import com.pottda.game.physicsBox2D.Box2DPhysicsProjectile;
import com.pottda.game.view.ViewActor;

import javax.vecmath.Vector2f;

import java.util.Collection;
import java.util.List;

import static com.pottda.game.model.ModelActor.ENEMY_TEAM;
import static com.pottda.game.model.ModelActor.PLAYER_TEAM;


/**
 * {@inheritDoc}
 * <p>
 * This implementation works on Box2D and LibGDX
 */
public class Box2DActorFactory extends ActorFactory {
    // Constants
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
    private static Filter PROJECTILE_PENETRATION_FILTER;

    private final static float PROJECTILE_SENSOR_RADIUS = 0.05f;
    private final static float PROJECTILE_SENSOR_DENSITY = 0f;

    private final static float OBSTACLE_FRICTION = 0.25f;
    private final static float OBSTACLE_BOUNCINESS = 0f;
    private static Filter OBSTACLE_FILTER;


    private final World world;

    private final Stage stage;

    private BodyDef characterBodyDef;
    private BodyDef projectileBodyDef;
    private BodyDef obstacleBodyDef;

    private FixtureDef characterFixtureDef;
    private FixtureDef projectileFixtureDef;
    private FixtureDef projectileSensorFixtureDef;
    private FixtureDef obstacleFixtureDef;

    private Collection<AbstractController> controllers;

    /**
     * @param world          a {@link World} to handle the Actor's {@link Body}
     * @param stage          a {@link Stage} to draw the {@link ViewActor on}
     * @param controllerList a {@link Collection} to add the controllers to
     */
    public Box2DActorFactory(World world, Stage stage,
                             Collection<AbstractController> controllerList) {
        this.world = world;
        this.stage = stage;
        this.controllers = controllerList;
        filterCategoryInit();
        bodyDefInit();
        fixtureDefInit();
    }

    @Override
    public AIController buildEnemy(Sprites sprite, Vector2f position, Inventory inventory) {
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
        body.setUserData(model);

        // Add inventory
        //model.inventory = inventory;

        ViewActor view = new ViewActor(sprite.texture);
        stage.addActor(view);
        AIController aiController = new DumbAIController(model, view);
        controllers.add(aiController);

        return aiController;
    }

    @Override
    public AbstractController buildPlayer(Sprites sprite, Vector2f position) {
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

        Character player = new Character(physics);
        DumbAIController.goal = player;
        player.team = PLAYER_TEAM;
        body.setUserData(player);

        // Add inventory
        try {
            player.inventory = InventoryFactory.createFromXML(Gdx.files.internal(
                    "inventoryblueprint/testInv2.xml").file());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ViewActor view = new ViewActor(sprite.texture);

        AbstractController controller = null;

        switch (ControllerOptions.controllerSettings) {
            case TOUCH_JOYSTICK:
                controller = new TouchJoystickController(player, view, ControllerOptions.joystickStage);
                break;
            case KEYBOARD_MOUSE:
                controller = new KeyboardMouseController(player, view, stage);
                break;
            case KEYBOARD_ONLY:
                controller = new KeyboardOnlyController(player, view);
                break;
        }
        stage.addActor(view);

        controllers.add(controller);
        return controller;
    }

    @Override
    public ProjectileController buildProjectile(Sprites sprite, int team, boolean bounces, boolean penetrates, Vector2f position) {
        // Create body
        Body body = world.createBody(projectileBodyDef);
        final float playerWidth = 25f * 0.0375f / 4f; // 25x25px
        body.setTransform(position.getX() + playerWidth, position.getY() + playerWidth, 0);

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
        body.setUserData(model);

        ViewActor view = new ViewActor(sprite.texture);

        stage.addActor(view);

        ProjectileController projectileController = new ProjectileController(model, view);
        controllers.add(projectileController);
        return projectileController;
    }

    @Override
    public AbstractController buildObstacle(Sprites sprite, Vector2f position, Vector2f size, boolean isBorder) {
        Body body = world.createBody(obstacleBodyDef);
        body.setTransform(position.getX(), position.getY(), 0);

        PolygonShape tempPolygon = new PolygonShape();
        if (isBorder) {
            if (size.x > size.y) {
                tempPolygon.setAsBox(size.x, size.y / 2); // setAsBox uses half-width and half-height as parameters
            } else {
                tempPolygon.setAsBox(size.x / 2, size.y);
            }
        } else {
            tempPolygon.setAsBox(size.x, size.y);
        }

        obstacleFixtureDef.shape = tempPolygon;

        body.createFixture(obstacleFixtureDef);

        Box2DPhysicsActor physics = new Box2DPhysicsActor(body);

        Obstacle model = new Obstacle(physics);
        body.setUserData(model);

        ViewActor view = new ViewActor(sprite.texture, size);

        stage.addActor(view);

        ObstacleController obstacleController = new ObstacleController(model, view);
        controllers.add(obstacleController);
        return obstacleController;
    }

    private void filterCategoryInit() {
        CHARACTER_PLAYER_FILTER = new Filter();
        CHARACTER_ENEMY_FILTER = new Filter();
        PROJECTILE_PLAYER_FILTER = new Filter();
        PROJECTILE_ENEMY_FILTER = new Filter();
        PROJECTILE_PENETRATION_FILTER = new Filter();
        OBSTACLE_FILTER = new Filter();

        CHARACTER_PLAYER_FILTER.categoryBits = 0x0001;
        CHARACTER_ENEMY_FILTER.categoryBits = 0x0002;
        PROJECTILE_PLAYER_FILTER.categoryBits = 0x0004;
        PROJECTILE_ENEMY_FILTER.categoryBits = 0x0008;
        PROJECTILE_PENETRATION_FILTER.categoryBits = 0x0010;
        OBSTACLE_FILTER.categoryBits = 0x0020;

        // Player collides with Enemy Character and Projectile and obstacle
        CHARACTER_PLAYER_FILTER.maskBits = (short) ((int) CHARACTER_ENEMY_FILTER.categoryBits +
                (int) PROJECTILE_ENEMY_FILTER.categoryBits + (int) OBSTACLE_FILTER.categoryBits);

        // Enemy collides with itself, Player Character, Projectile and obstacle
        CHARACTER_ENEMY_FILTER.maskBits = (short) ((int) CHARACTER_PLAYER_FILTER.categoryBits +
                (int) CHARACTER_ENEMY_FILTER.categoryBits + (int) PROJECTILE_PLAYER_FILTER.categoryBits + (int) OBSTACLE_FILTER.categoryBits);

        // Player Projectile collides with Enemy Character and obstacle
        PROJECTILE_PLAYER_FILTER.maskBits = (short) ((int) CHARACTER_ENEMY_FILTER.categoryBits + (int) OBSTACLE_FILTER.categoryBits);

        // Enemy Projectile collides with Player Character and obstacle
        PROJECTILE_ENEMY_FILTER.maskBits = (short) ((int) CHARACTER_PLAYER_FILTER.categoryBits + (int) OBSTACLE_FILTER.categoryBits);

        // Projectiles with the 'penetrates' flag only collide with the terrain and obstacle
        PROJECTILE_PENETRATION_FILTER.maskBits = OBSTACLE_FILTER.categoryBits;
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
