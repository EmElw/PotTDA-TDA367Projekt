package com.pottda.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.pottda.game.controller.AIController;
import com.pottda.game.controller.AbstractController;
import com.pottda.game.controller.Box2DActorFactory;
import com.pottda.game.controller.ControllerOptions;
import com.pottda.game.model.WaveController;
import com.pottda.game.model.ActorFactory;
import com.pottda.game.model.Character;
import com.pottda.game.physicsBox2D.CollisionListener;
import com.pottda.game.view.*;

import javax.vecmath.Vector2f;

import java.util.*;

import static com.pottda.game.PoTDA.GameState.*;
import static com.pottda.game.controller.ControllerOptions.ControllerMode.*;

public class PoTDA extends ApplicationAdapter {
    private Stage hudStage;
    private Stage joystickStage;
    private Stage gameStage;
    private Stage mainMenuStage;
    private OrthographicCamera camera;

    /*
    A List of controllers to iterate through every game-update
     */
    private Set<AbstractController> controllers;
    /*
    A Stack to buffer new controllers created during the list-iteration
     */
    private Stack<AbstractController> controllerBuffer;
    /*
    A Stack to buffer controllers that should be removed during rendering
     */
    private Stack<AbstractController> controllerRemovalBuffer;

    private World world;
    private float accumulator;

    private HUDView hudView;
    private SoundsAndMusic soundsAndMusic;
    private GameView gameView;
    private Box2DActorFactory box2DActorFactory;
    private MainMenuView mainMenuView;

    private WaveController waveController;


    public enum GameState {
        NONE,
        RUNNING,
        WAITING,
        PAUSED,
        OPTIONS,
        MAIN_MENU,
        MAIN_CHOOSE,
        MAIN_CONTROLS
    }

    private static GameState gameState = NONE;

    private static final String GAME_TITLE = "Panic on TDAncefloor";

    public static final float WIDTH = 800;
    public static final float HEIGHT = 480;
    public static final float WIDTH_METERS = 30;
    public static final float HEIGHT_METERS = 18;
    public static final float HEIGHT_RATIO = WIDTH_METERS / WIDTH;
    public static final float WIDTH_RATIO = HEIGHT_METERS / HEIGHT;
    private static final float scaling = 1.2f;

    @Override
    public void create() {
        Gdx.graphics.setTitle(GAME_TITLE);
        camera = new OrthographicCamera();

        hudStage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        joystickStage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        gameStage = new Stage(new StretchViewport(WIDTH_METERS, HEIGHT_METERS));
        gameStage.getCamera().position.x = WIDTH_METERS / 2;
        gameStage.getCamera().position.y = HEIGHT_METERS / 2;
        mainMenuStage = new Stage(new StretchViewport(WIDTH, HEIGHT));

        gameState = MAIN_MENU;
        Gdx.input.setInputProcessor(mainMenuStage);
        Box2D.init();

        mainMenuView = new MainMenuView(mainMenuStage);

        waveController = new WaveController(WIDTH_METERS, HEIGHT_METERS, scaling);
    }

    /**
     * Inits the game world and player
     */
    private void doOnStartGame() {
        controllers = new HashSet<AbstractController>();
        controllerBuffer = new Stack<AbstractController>();
        controllerRemovalBuffer = new Stack<AbstractController>();

        hudView = new HUDView(hudStage);
        world = new World(new Vector2(0, 0), false);
        world.setContactListener(new CollisionListener());
        accumulator = 0;

        gameView = new GameView(gameStage, joystickStage);

        soundsAndMusic = new SoundsAndMusic();
        startMusic();

        // Create and set ActorFactory implementation
        box2DActorFactory = new Box2DActorFactory(world, gameStage, controllerBuffer);
        ActorFactory.setFactory(box2DActorFactory);

        createPlayer();

        gameState = WAITING;

        createWorldBorders();

        waveController.initNextLevel();
    }

    /**
     * Creates the player
     */
    private void createPlayer() {
        // Add player
        ActorFactory.get().buildPlayer(com.pottda.game.model.Sprites.PLAYER,
                new Vector2f(WIDTH_METERS * scaling / 2, HEIGHT_METERS * scaling / 2));

    }



    /**
     * Checks if any enemies are alive
     * @return true if at least one enemy is alive
     */
    private boolean enemiesAlive() {
        for (AbstractController a : controllers) {
            if (a instanceof AIController) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates four obstacles around the playing area
     */
    private void createWorldBorders() {
        final float border_thickness = 25f;
        // Scale the area bigger or smaller
        final float area_scaling = 1.2f;
        final float right_border_extra = 0.78f;
        // Bottom
        controllers.add(ActorFactory.get().buildObstacle(com.pottda.game.model.Sprites.BORDER,
                new Vector2f(0, 0), new Vector2f(WIDTH_METERS * area_scaling, border_thickness * HEIGHT_RATIO), true));
        // Left
        controllers.add(ActorFactory.get().buildObstacle(com.pottda.game.model.Sprites.BORDER,
                new Vector2f(0, 0), new Vector2f(border_thickness * WIDTH_RATIO, HEIGHT_METERS * area_scaling), true));
        // Top
        controllers.add(ActorFactory.get().buildObstacle(com.pottda.game.model.Sprites.BORDER,
                new Vector2f(0, HEIGHT_METERS * area_scaling), new Vector2f(WIDTH_METERS * area_scaling, border_thickness * HEIGHT_RATIO), true));
        // Right
        controllers.add(ActorFactory.get().buildObstacle(com.pottda.game.model.Sprites.BORDER,
                new Vector2f(WIDTH_METERS * area_scaling, 0), new Vector2f(border_thickness * WIDTH_RATIO, (HEIGHT_METERS + right_border_extra) * area_scaling), true));
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, HEIGHT * width / (float) height, HEIGHT);

        hudStage.getViewport().update(width, height, false);
        gameStage.getViewport().update(width, height, false);
        joystickStage.getViewport().update(width, height, false);
        mainMenuStage.getViewport().update(width, height, false);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        checkTouch();


        switch (gameState) {
            case NONE:
                break;
            case RUNNING:
                // Update the model
                updateGame();
                updateWorld();
                if (!enemiesAlive()) {
                    if (waveController.finishedWaves()) {
                        // TODO Go to inventory
                        System.out.println("To inventory");
                        waveController.initNextLevel();
                        gameState = WAITING;
                    } else {
                        gameState = WAITING;
                        waveController.setStartTime(System.currentTimeMillis());
                    }
                }
                break;
            case WAITING:
                updateGame();
                updateWorld();
                // Check if user has waited 5 seconds
                if (waveController.waited()) {
                    gameState = RUNNING;
                    // Start next wave
                    waveController.startWave();
                }
                break;
            case PAUSED:
                // Draw the pause menu
                hudView.renderPaused();
                hudStage.draw();
                break;
            case OPTIONS:
                // Draw the options menu
                hudView.renderOptions();
                hudStage.draw();
                break;
            case MAIN_MENU:
                // Draw the main menu
                mainMenuView.renderMainMenu();
                break;
            case MAIN_CHOOSE:
                // Draw the choose difficulty menu
                mainMenuView.renderChooseDiff();
                break;
            case MAIN_CONTROLS:
                // Draw the choose controller menu
                mainMenuView.renderChooseControls();
                break;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            switch (gameState) {
                case RUNNING:
                    gameState = PAUSED;
                    break;
                case PAUSED:
                    gameState = RUNNING;
                    break;
                case OPTIONS:
                    gameState = PAUSED;
                    break;
            }
        }

    }

    /**
     * Updates the game logic
     */
    private void updateGame() {
        if (controllers != null) {
            bringOutYourDead();

            // Update all controllers, causing the model to update
            for (AbstractController c : controllers) {
                c.onNewFrame();
            }
            // Add created during the cycle to the list
            // TODO could potentially do some juggling to also execute the onFrame for all o these
            controllers.addAll(controllerBuffer);
            controllerBuffer.clear();
        }
    }

    /**
     * Updates physics, health bar and renders views
     */
    private void updateWorld() {
        // Update the physics world
        doPhysicsStep(Gdx.graphics.getDeltaTime());

        // Set the health bar to player's current health
        hudView.setHealthbar(Character.player.getCurrentHealth());

        // Draw the game
        gameView.render();
        hudView.renderRunning();
        hudStage.draw();
    }

    /**
     * Removes actors that have flagged themselvs as dead
     */
    private void bringOutYourDead() {
        // Prepare removal of "dead" actors
        for (AbstractController c : controllers) {
            if (c.shouldBeRemoved()) {
                prepareForRemoval(c);
            }
        }

        // Remove "dead" actors
        if (controllerRemovalBuffer.size() > 0) {
            controllers.removeAll(controllerRemovalBuffer);
            controllerRemovalBuffer.clear();
        }
    }

    /**
     * Checks if the player touches any HUD elements
     */
    private void checkTouch() {
        if (Gdx.input.justTouched()) { // Only check first touch
            // Get hudStage camera and unproject to get correct coordinates!
            Vector3 vector3 = hudStage.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            switch (gameState) {
                case RUNNING:
                    if (hudView.checkIfTouchingPauseButton(vector3)) {
                        // Touching pause button
                        gameState = PAUSED;
                    }
                    break;
                case PAUSED:
                    if (hudView.checkIfTouchingPauseResume(vector3)) {
                        // Touching pause resume
                        gameState = RUNNING;
                    } else if (hudView.checkIfTouchingPauseOptions(vector3)) {
                        // Touching pause options
                        gameState = OPTIONS;
                    } else if (hudView.checkIfTouchingPauseQuit(vector3)) {
                        // Touching pause quit
                        Gdx.app.exit();
                    }
                    break;
                case OPTIONS:
                    if (hudView.checkIfTouchingOptionsReturn(vector3)) {
                        // Touched
                        gameState = PAUSED;
                    } else if (hudView.checkIfTouchingOptionsMusic(vector3)) {
                        soundsAndMusic.setMusicVolume(hudView.getNewMusicVolume(vector3));
                    } else if (hudView.checkIfTouchingOptionsSFX(vector3)) {
                        soundsAndMusic.setSFXVolume(hudView.getNewSFXVolume(vector3));
                    }
                    break;
                case MAIN_MENU:
                    if (mainMenuView.checkIfTouchingStart(vector3)) {
                        gameState = MAIN_CONTROLS;
                    } else if (mainMenuView.checkIfTouchingQuit(vector3)) {
                        // Exit game
                        Gdx.app.exit();
                    }
                    break;
                case MAIN_CHOOSE:
                    if (mainMenuView.checkIfTouchingEasy(vector3)) {
                        // TODO Set easy mode
                        doOnStartGame();
                        gameState = RUNNING;
                        Gdx.input.setInputProcessor(joystickStage);
                    } else if (mainMenuView.checkIfTouchingHard(vector3)) {
                        // TODO Set hard mode
                        doOnStartGame();
                        gameState = RUNNING;
                        Gdx.input.setInputProcessor(joystickStage);
                    }
                    break;
                case MAIN_CONTROLS:
                    if (mainMenuView.checkIfTouchingTouch(vector3)) {
                        gameState = MAIN_CHOOSE;
                        ControllerOptions.controllerSettings = TOUCH_JOYSTICK;
                        ControllerOptions.joystickStage = joystickStage;
                    } else if (mainMenuView.checkIfTouchingKeyboardOnly(vector3)) {
                        gameState = MAIN_CHOOSE;
                        ControllerOptions.controllerSettings = KEYBOARD_ONLY;
                    } else if (mainMenuView.checkIfTouchingKeyboardMouse(vector3)) {
                        gameState = MAIN_CHOOSE;
                        ControllerOptions.controllerSettings = KEYBOARD_MOUSE;
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

    /**
     * Plays music
     */
    private void startMusic() {
        soundsAndMusic.play();
    }

    @Override
    public void dispose() {
        hudStage.dispose();
        mainMenuView.dispose();
        if (hudView != null) {
            hudView.dispose();
        }
        if (world != null) {
            world.dispose();
        }
        if (soundsAndMusic != null) {
            soundsAndMusic.dispose();
        }
        if (gameView != null) {
            gameView.dispose();
        }
    }

    private void prepareForRemoval(AbstractController controller) {
        controller.getModel().getPhysicsActor().destroyBody();
        controller.getView().remove();
        controllerRemovalBuffer.add(controller);
    }
}
