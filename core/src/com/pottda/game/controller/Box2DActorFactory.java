package com.pottda.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.XmlReader;
import com.pottda.game.model.*;
import com.pottda.game.model.Character;
import com.pottda.game.physicsBox2D.Box2DPhysicsActor;
import com.pottda.game.physicsBox2D.Box2DPhysicsCharacter;
import com.pottda.game.physicsBox2D.Box2DPhysicsProjectile;
import com.pottda.game.view.ActorView;
import com.pottda.game.view.Sprites;

import javax.vecmath.Vector2f;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.util.ArrayList;
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
     * @param stage          a {@link Stage} to draw the {@link ActorView on}
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
    public AIController buildEnemy(Sprites sprite, Vector2f position, String xmlFilePath) {
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
        try {
            model.inventory = getInventory(xmlFilePath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ActorView view = new ActorView(sprite.texture);
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

        try {
            player.inventory = getInventory("inventoryblueprint/testInv2.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ActorView view = new ActorView(sprite.texture);

        AbstractController controller = null;

        switch (ControllerOptions.controllerSettings) {
            case TOUCH_JOYSTICK:
                controller = new TouchJoystickController(player, view, ControllerOptions.joystickStage);
                break;
            case KEYBOARD_MOUSE:
//            case KEYBOARD_ONLY:
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

    /**
     * Creates and returns a new inventory
     *
     * @param filePath path to the xml file to get inventory from
     * @return a new inventory from the given xml file
     * @throws ClassNotFoundException
     * @throws ParserConfigurationException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    private Inventory getInventory(String filePath) throws ClassNotFoundException, ParserConfigurationException, InstantiationException, IllegalAccessException, IOException {
        List<XMLItem> xmlItemList = new ArrayList<XMLItem>();

        FileHandle file = Gdx.files.internal(filePath);
        // Create the inventory to return
        XmlReader xml = new XmlReader();
        XmlReader.Element xml_element = null;
        try {
            // Read the file
            xml_element = xml.parse(file);
            // If the loaded file does not contain an inventory tag, throw exception
            if (!xml_element.toString().split("\n")[0].contains("inventory")) {
                throw new IOException("Couldn't find <inventory> tag");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert xml_element != null;
        String secondLine = xml_element.toString().split("\n")[0];
        // Get w and h from XML file
        final int width = Integer.parseInt(secondLine.split("\"")[1]);
        final int height = Integer.parseInt(secondLine.split("\"")[3]);

        // Set the dimensions of the inventory
        Inventory inventory = new Inventory();
        inventory.setDimensions(width, height);

        // Create the XMLItem list
        for (String s : xml_element.toString().split("\n")) {
            if (s.contains("<item ")) {
                int orientation = Integer.parseInt(s.split("\"")[1]);
                int x = Integer.parseInt(s.split("\"")[3]);
                int y = Integer.parseInt(s.split("\"")[5]);
                String name = s.split("\"")[7];
                XMLItem xmlItem = new XMLItem(name, x, y, orientation);
                xmlItemList.add(xmlItem);
            }
        }

        return InventoryFactory.createFromXML(xmlItemList, inventory, file.name());
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

        ActorView view = new ActorView(sprite.texture);

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

        ActorView view = new ActorView(sprite.texture, size);

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
        CHARACTER_PLAYER_FILTER.maskBits = (short) (CHARACTER_PLAYER_FILTER.categoryBits |
                CHARACTER_ENEMY_FILTER.categoryBits |
                PROJECTILE_ENEMY_FILTER.categoryBits |
                OBSTACLE_FILTER.categoryBits);

        // Enemy collides with itself, Player Character, Projectile and obstacle
        CHARACTER_ENEMY_FILTER.maskBits = (short)(CHARACTER_PLAYER_FILTER.categoryBits |
                CHARACTER_ENEMY_FILTER.categoryBits |
                PROJECTILE_PLAYER_FILTER.categoryBits |
                OBSTACLE_FILTER.categoryBits);

        // Player Projectile collides with Enemy Character and obstacle
        PROJECTILE_PLAYER_FILTER.maskBits = (short) (CHARACTER_ENEMY_FILTER.categoryBits |
                OBSTACLE_FILTER.categoryBits);

        // Enemy Projectile collides with Player Character and obstacle
        PROJECTILE_ENEMY_FILTER.maskBits = (short) (CHARACTER_PLAYER_FILTER.categoryBits |
                OBSTACLE_FILTER.categoryBits);

        // Projectiles with the 'penetrates' flag only collide with the terrain and obstacle
        PROJECTILE_PENETRATION_FILTER.maskBits = OBSTACLE_FILTER.categoryBits;

        // Obstacles collide with everything
        OBSTACLE_FILTER.maskBits = (short) (CHARACTER_PLAYER_FILTER.categoryBits |
                CHARACTER_ENEMY_FILTER.categoryBits |
                PROJECTILE_PLAYER_FILTER.categoryBits |
                PROJECTILE_ENEMY_FILTER.categoryBits |
                PROJECTILE_PENETRATION_FILTER.categoryBits);
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
