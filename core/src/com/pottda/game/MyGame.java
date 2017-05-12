package com.pottda.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.pottda.game.View.Sprites;
import com.pottda.game.actorFactory.Box2DActorFactory;
import com.pottda.game.controller.ControllerOptions;
import com.pottda.game.model.ActorFactory;
import com.pottda.game.model.InventoryFactory;
import com.pottda.game.physicsBox2D.CollisionListener;
import com.pottda.game.view.GameView;
import com.pottda.game.view.HUDView;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.pottda.game.controller.AbstractController;
import com.pottda.game.controller.TouchJoystickController;
import com.pottda.game.view.MainMenuView;
import com.pottda.game.view.SoundsAndMusic;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2f;

public class MyGame extends ApplicationAdapter {
    private Stage hudStage;
    private Stage joystickStage;
    private Stage gameStage;
    private Stage mainMenuStage;
    private OrthographicCamera camera;

    private float accumulator;

    private List<AbstractController> controllers;
    private World world;

    private HUDView hudView;
    private SoundsAndMusic soundsAndMusic;
    private GameView gameView;
    private Box2DActorFactory box2DActorFactory;
    private MainMenuView mainMenuView;

    private static final int RUNNING = 1;
    private static final int PAUSED = 2;
    private static final int OPTIONS = 3;
    private static final int MAIN_MENU = 4;
    private static final int MAIN_CHOOSE = 5;
    private static int GAME_STATE = 0;

    private static final String playerImage = "circletest.png"; // change later
    private static final String enemyImage = "circletestred.png";
    private static final String borderImage = "game/border.png";

    private static final String playerStartInventory = "inventoryblueprint/playerStartInventory.xml";

    private static final String GAME_TITLE = "Panic on TDAncefloor";

    public static final float WIDTH = 800;
    public static final float HEIGHT = 480;
    public static final float WIDTH_METERS = 30;
    public static final float HEIGHT_METERS = 18;
    public static final float HEIGHT_RATIO = WIDTH_METERS / WIDTH;
    public static final float WIDTH_RATIO = HEIGHT_METERS / HEIGHT;

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

        GAME_STATE = MAIN_MENU;
        Gdx.input.setInputProcessor(mainMenuStage);
        Box2D.init();

        mainMenuView = new MainMenuView(mainMenuStage);
    }

    private void doOnStartGame() {
        controllers = new ArrayList<AbstractController>();

        hudView = new HUDView(hudStage);
        world = new World(new Vector2(0, 0), false);
        world.setContactListener(new CollisionListener());
        accumulator = 0;

        gameView = new GameView(gameStage, joystickStage);

        soundsAndMusic = new SoundsAndMusic();
        startMusic();

        // Create and set ActorFactory implementation
        box2DActorFactory = new Box2DActorFactory(world, gameStage);
        ActorFactory.setFactory(box2DActorFactory);

        ControllerOptions.joystickStage = joystickStage;

        if (Gdx.app.getType() == Application.ApplicationType.Android) { // if on android
            ControllerOptions.controllerSettings = ControllerOptions.TOUCH_JOYSTICK;
        } else if (Gdx.app.getType() == Application.ApplicationType.Desktop) { // if on desktop
            ControllerOptions.controllerSettings = ControllerOptions.KEYBOARD_MOUSE;
        }

        // Add player to controller list
        controllers.add(ActorFactory.get().buildPlayer(Sprites.PLAYER, new Vector2f(WIDTH_METERS / 2, HEIGHT_METERS / 2)));

        final float scaling = 1.2f;

        // Add some enemies
        for (int i = 0; i < 5; i++) {
            try {
                controllers.add(ActorFactory.get().buildEnemy(Sprites.ENEMY, //Change
                        new Vector2f((float) (Math.random() * (WIDTH_METERS * scaling)), (float) (Math.random() * (HEIGHT_METERS * scaling))),
                        InventoryFactory.createFromXML(Gdx.files.internal(playerStartInventory).file())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        createWorldBorders();
    }

    /**
     * Creates four obstacles around the playing area
     */
    private void createWorldBorders() {
        final float border_thickness = 0.5f;
        // Scale the area bigger or smaller
        final float area_scaling = 1.2f;
        // Bottom
        controllers.add(ActorFactory.get().buildObstacle(Sprites.BORDER,
                new Vector2f(0, 0), new Vector2f(WIDTH_METERS * area_scaling, border_thickness)));
        // Left
        controllers.add(ActorFactory.get().buildObstacle(Sprites.BORDER,
                new Vector2f(0, 0), new Vector2f(border_thickness, HEIGHT_METERS * area_scaling)));
        // Top
        controllers.add(ActorFactory.get().buildObstacle(Sprites.BORDER,
                new Vector2f(0, HEIGHT_METERS * area_scaling), new Vector2f(WIDTH_METERS * area_scaling, border_thickness)));
        // Right
        controllers.add(ActorFactory.get().buildObstacle(Sprites.BORDER,
                new Vector2f(WIDTH_METERS * area_scaling, 0), new Vector2f(border_thickness, HEIGHT_METERS * area_scaling)));
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
        } else if (GAME_STATE == MAIN_MENU) {
            mainMenuView.renderMainMenu();
        } else if (GAME_STATE == MAIN_CHOOSE) {
            mainMenuView.renderChooseDiff();
        }

        if (GAME_STATE < MAIN_MENU) {
            hudStage.draw();
        }
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
                case MAIN_MENU:
                    if (mainMenuView.checkIfTouchingStart(vector3)) {
                        GAME_STATE = MAIN_CHOOSE;
                    } else if (mainMenuView.checkIfTouchingQuit(vector3)) {
                        // Exit game
                        Gdx.app.exit();
                    }
                    break;
                case MAIN_CHOOSE:
                    if (mainMenuView.checkIfTouchingEasy(vector3)) {
                        // TODO Set easy mode
                        doOnStartGame();
                        GAME_STATE = RUNNING;
                        Gdx.input.setInputProcessor(joystickStage);
                    } else if (mainMenuView.checkIfTouchingHard(vector3)) {
                        // TODO Set hard mode
                        doOnStartGame();
                        GAME_STATE = RUNNING;
                        Gdx.input.setInputProcessor(joystickStage);
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
        hudView.dispose();
        hudStage.dispose();
        world.dispose();
        soundsAndMusic.dispose();
        gameView.dispose();
        mainMenuView.dispose();
    }
}
