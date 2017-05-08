package com.pottda.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pottda.game.controller.AbstractController;
import com.pottda.game.controller.KeyboardMouseController;
import com.pottda.game.controller.TouchJoystickController;
import com.pottda.game.model.Character;
import com.pottda.game.model.ModelActor;
import com.pottda.game.physicsBox2D.Box2DPhysicsActor;
import com.pottda.game.physicsBox2D.Box2DPhysicsCharacter;
import com.pottda.game.view.HUDView;
import com.pottda.game.view.SoundsAndMusic;
import com.pottda.game.view.ViewActor;

import java.util.ArrayList;
import java.util.List;

public class MyGame extends ApplicationAdapter {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 480;
    private Stage stage;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Batch spriteBatch;

    private Texture img;
    //private Box2DDebugRenderer box2DDebugRenderer;
    private float accumulator;

    private BodyDef bodyDef;
    private Body body;
    private Shape shape;
    private FixtureDef fixtureDef;
    private Fixture fixture;
    private ShapeRenderer shapeRenderer;
    private Sprite sprite;

    private List<AbstractController> controllers;
    private World world;

    private HUDView hudView;
    private SoundsAndMusic soundsAndMusic;

    private static final int RUNNING = 1;
    private static final int PAUSED = 2;
    private static final int OPTIONS = 3;
    private static int GAME_STATE = 0;

    @Override
    public void create() {
        controllers = new ArrayList<AbstractController>();

        camera = new OrthographicCamera(WIDTH, HEIGHT);
        camera.setToOrtho(true, WIDTH, HEIGHT); // Y-axis pointing down
        viewport = new FitViewport(WIDTH, HEIGHT, camera);
        stage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        spriteBatch = new SpriteBatch();

        GAME_STATE = RUNNING;
        Gdx.input.setInputProcessor(stage);
        Box2D.init();
        //box2DDebugRenderer = new Box2DDebugRenderer();
        world = new World(new Vector2(0, 0), false);
        accumulator = 0;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(400, 240);

        /*body = world.createBody(bodyDef);
        shape = new CircleShape();
        shape.setRadius(2.5f);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;

        fixture = body.createFixture(fixtureDef);
        shape.dispose();*/

        img = new Texture(Gdx.files.internal("CircleTest.png"));
        sprite = new Sprite(img);
        shapeRenderer = new ShapeRenderer();

        hudView = new HUDView(stage);

        //if (Gdx.app.getType() == Application.ApplicationType.Android) { // if on android
        controllers.add(new TouchJoystickController(new Character(new Box2DPhysicsCharacter(world.createBody(bodyDef))), new ViewActor(), stage));
        //} else if (Gdx.app.getType() == Application.ApplicationType.Desktop) { // if on desktop
        // Check if using mouse?
        //abstractController = new KeyboardOnlyController(new ArrayList<AttackListener>(), new ArrayList<MovementListener>(), false);
        //    controllers.add(new KeyboardMouseController(new Character(new Box2DPhysicsCharacter(world.createBody(bodyDef))), new ViewActor()));
        //}

        soundsAndMusic = new SoundsAndMusic();
        startMusic();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        checkTouch();

        if (controllers != null) {
            // Update all controllers, causing the model to update
            for (AbstractController c : controllers) {
                if (c instanceof TouchJoystickController && GAME_STATE != RUNNING) {
                    // Render joysticks
                    c.onNewFrame();
                } else if (GAME_STATE == RUNNING) {
                    c.onNewFrame();
                }
            }
        }

        if (GAME_STATE == PAUSED) {
            hudView.renderPaused();

        } else if (GAME_STATE == RUNNING) {
            hudView.renderRunning();

            // Update the world
            doPhysicsStep(Gdx.graphics.getDeltaTime());

        } else if (GAME_STATE == OPTIONS) {
            hudView.renderOptions();
        }

        // Draw stuff
        spriteBatch.begin();
        sprite.draw(spriteBatch);
        spriteBatch.end();

        stage.draw();
    }

    public static int getGameState() {
        return GAME_STATE;
    }

    /**
     * Checks if the player touches any HUD elements
     */
    private void checkTouch() {
        if (Gdx.input.justTouched()) { // Only check first touch
            // Get stage camera and unproject to get correct coordinates!
            Vector3 vector3 = stage.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            switch (GAME_STATE) {
                case RUNNING:
                    if (hudView.checkIfTouchingPauseButton(vector3)) {
                        // Touching pause button
                        GAME_STATE = PAUSED;
                    }
                    break;
                case PAUSED:
                    if (hudView.checkIfTouchingPauseResume(vector3)) {
                        // Touching pause resume
                        GAME_STATE = RUNNING;
                    } else if (hudView.checkIfTouchingPauseOptions(vector3)) {
                        // Touching pause options
                        GAME_STATE = OPTIONS;
                    } else if (hudView.checkIfTouchingPauseQuit(vector3)) {
                        // Touching pause quit
                        Gdx.app.exit();
                    }
                    break;
                case OPTIONS:
                    if (hudView.checkIfTouchingOptionsReturn(vector3)) {
                        // Touched
                        GAME_STATE = PAUSED;
                    } else if (hudView.checkIfTouchingOptionsMusic(vector3)) {
                        soundsAndMusic.setMusicVolume(hudView.getNewMusicVolume(vector3));
                    } else if (hudView.checkIfTouchingOptionsSFX(vector3)) {
                        soundsAndMusic.setSFXVolume(hudView.getNewSFXVolume(vector3));
                    }
                    break;
            }

        }
    }

    private void doPhysicsStep(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= (1.0f / 60.0f)) {
            world.step(1.0f / 60.0f, 6, 2);
            accumulator -= 1.0f / 60.0f;
        }
    }

    private void startMusic() {
        soundsAndMusic.play();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        img.dispose();
        //shape.dispose(); // Causes EXCEPTION_ACCESS_VIOLATION in JRE
        shapeRenderer.dispose();
        hudView.dispose();
        stage.dispose();
        world.dispose();
        soundsAndMusic.dispose();
    }
}
