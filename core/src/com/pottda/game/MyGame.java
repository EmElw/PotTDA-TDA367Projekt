package com.pottda.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.pottda.game.actorFactory.Box2DActorFactory;
import com.pottda.game.controller.ControllerOptions;
import com.pottda.game.model.ActorFactory;
import com.pottda.game.model.InventoryFactory;
import com.pottda.game.view.GameView;
import com.pottda.game.view.HUDView;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pottda.game.controller.AbstractController;
import com.pottda.game.controller.TouchJoystickController;
import com.pottda.game.view.SoundsAndMusic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2f;
import javax.xml.parsers.ParserConfigurationException;

public class MyGame extends ApplicationAdapter {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 480;
    private Stage hudStage;
    private Stage joystickStage;
    private Stage gameStage;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Batch spriteBatch;

    private float accumulator;

    private List<AbstractController> controllers;
    private World world;

    private HUDView hudView;
    private SoundsAndMusic soundsAndMusic;
    private GameView gameView;
    private Box2DActorFactory box2DActorFactory;

    private static final int RUNNING = 1;
    private static final int PAUSED = 2;
    private static final int OPTIONS = 3;
    private static int GAME_STATE = 0;

    private static final String playerImage = "CircleTest.png"; // change later
    private static final String enemyImage = "CircleTestRed.png";

    @Override
    public void create() {
        controllers = new ArrayList<AbstractController>();

        camera = new OrthographicCamera(WIDTH, HEIGHT);
        camera.setToOrtho(true, WIDTH, HEIGHT); // Y-axis pointing down
        viewport = new FitViewport(WIDTH, HEIGHT, camera);
        hudStage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        joystickStage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        gameStage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        spriteBatch = new SpriteBatch();

        GAME_STATE = RUNNING;
        Gdx.input.setInputProcessor(hudStage);
        Box2D.init();
        world = new World(new Vector2(0, 0), false);
        accumulator = 0;

        hudView = new HUDView(hudStage);
        gameView = new GameView(gameStage, joystickStage);
        soundsAndMusic = new SoundsAndMusic();
        startMusic();


        // Create and set ActorFactory implementation
        box2DActorFactory = new Box2DActorFactory(world);
        ActorFactory.setFactory(box2DActorFactory);

        ControllerOptions.joystickStage = joystickStage;

        if (Gdx.app.getType() == Application.ApplicationType.Android) { // if on android
            ControllerOptions.controllerSettings = ControllerOptions.TOUCH_JOYSTICK;
        } else if (Gdx.app.getType() == Application.ApplicationType.Desktop) { // if on desktop
            ControllerOptions.controllerSettings = ControllerOptions.KEYBOARD_MOUSE;
        }

        // Add player to controller list
        controllers.add(ActorFactory.get().buildPlayer(gameStage,
                new Texture(Gdx.files.internal(playerImage)), new Vector2f(gameStage.getWidth() / 2, gameStage.getHeight() / 2)));

        // Add some enemies
        for (int i = 0; i < 5; i++) {
            try {
                controllers.add(ActorFactory.get().buildEnemy(gameStage, new Texture(Gdx.files.internal(enemyImage)), //Change
                        new Vector2f((float) Math.random() * gameStage.getWidth(), (float) Math.random() * gameStage.getHeight()),
                        InventoryFactory.createFromXML(Gdx.files.internal(
                                "inventoryblueprint/playerStartInventory.xml").file())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        hudStage.getViewport().update(width, height, false);
        gameStage.getViewport().update(width, height, false);
        joystickStage.getViewport().update(width, height, false);
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
            gameView.render();

            hudView.renderRunning();

            // Update the world
            doPhysicsStep(Gdx.graphics.getDeltaTime());

        } else if (GAME_STATE == OPTIONS) {
            hudView.renderOptions();
        }

        hudStage.draw();
    }

    /**
     * Returns the game state
     *
     * @return 1=running, 2=paused, 3=options
     */
    public static int getGameState() {
        return GAME_STATE;
    }

    /**
     * Checks if the player touches any HUD elements
     */
    private void checkTouch() {
        if (Gdx.input.justTouched()) { // Only check first touch
            // Get hudStage camera and unproject to get correct coordinates!
            Vector3 vector3 = hudStage.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
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
        hudView.dispose();
        hudStage.dispose();
        world.dispose();
        soundsAndMusic.dispose();
        gameView.dispose();
    }
}
