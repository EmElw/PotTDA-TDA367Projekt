package com.pottda.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.pottda.game.controller.*;
import com.pottda.game.model.*;
import com.pottda.game.model.Character;
import com.pottda.game.model.Sprites;
import com.pottda.game.model.builders.AbstractModelBuilder;
import com.pottda.game.model.builders.CharacterBuilder;
import com.pottda.game.model.builders.ObstacleBuilder;
import com.pottda.game.physicsBox2D.Box2DPhysicsActorFactory;
import com.pottda.game.physicsBox2D.CollisionListener;
import com.pottda.game.view.*;

import javax.vecmath.Vector2f;

import java.util.*;

import static com.pottda.game.PoTDA.GameState.*;
import static com.pottda.game.controller.ControllerOptions.ControllerMode.*;

public class PoTDA extends ApplicationAdapter implements NewControllerListener, ScoreChangeListener, DeathListener {
    private Stage hudStage;
    private Stage joystickStage;
    private Stage gameStage;
    private Stage pausedStage;
    private Stage optionsStage;
    private Stage mainMenuStage;
    private Stage mainControlsStage;
    private Stage mainDifficultyStage;
    private Stage gameOverStage;
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
    private PausedView pausedView;
    private OptionsView optionsView;
    private MainMenuView mainMenuView;
    private MainControlsView mainControlsView;
    private MainDifficultyView mainDifficultyView;
    private SoundsAndMusic soundsAndMusic;
    private GameView gameView;
    private GameOverView gameOverView;

    private WaveController waveController;
    private long startWaitInventory;

    @Override
    public void onNewController(AbstractController c) {
        controllerBuffer.add(c);
    }

    @Override
    public void scoreChanged(int points) {
        score += points;
        System.out.println("Score: " + score);
    }

    @Override
    public void onDeath() {
        enemyAmount--;
        System.out.println("Enemies alive: " + enemyAmount);
    }


    public enum GameState {
        NONE,
        RUNNING,
        PAUSED,
        OPTIONS,
        MAIN_MENU,
        MAIN_CHOOSE,
        MAIN_CONTROLS,
        WAITING_FOR_INVENTORY, GAME_OVER
    }

    private static GameState gameState;

    private static final String GAME_TITLE = "Panic on TDAncefloor";

    // Scale the play area
    private static final float SCALING = 2f;

    private static final float WIDTH = 800;
    private static final float HEIGHT = 480;
    private static final float WIDTH_METERS = 30 * SCALING;
    private static final float HEIGHT_METERS = 18 * SCALING;
    public static final float HEIGHT_RATIO = WIDTH_METERS / WIDTH / SCALING;
    public static final float WIDTH_RATIO = HEIGHT_METERS / HEIGHT / SCALING;

    private long startWaitGameOver;
    private static final long WAITING_TIME_GAME_OVER_SECONDS = 3;

    public static int score;
    private int enemyAmount;

    private Label label;
    private static String scoreLabelText = "Score: ";

    @Override
    public void create() {
        Gdx.graphics.setTitle(GAME_TITLE);
        camera = new OrthographicCamera();

        hudStage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        joystickStage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        gameStage = new Stage(new StretchViewport(WIDTH_METERS / SCALING, HEIGHT_METERS / SCALING));
        gameStage.getCamera().position.x = WIDTH_METERS / 2 / SCALING;
        gameStage.getCamera().position.y = HEIGHT_METERS / 2 / SCALING;
        mainMenuStage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        pausedStage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        optionsStage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        mainControlsStage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        mainDifficultyStage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        gameOverStage = new Stage(new StretchViewport(WIDTH, HEIGHT));

        gameState = MAIN_MENU;
        Gdx.input.setInputProcessor(mainMenuStage);
        Box2D.init();

        mainMenuView = new MainMenuView(mainMenuStage);
        mainDifficultyView = new MainDifficultyView(mainDifficultyStage);
        mainControlsView = new MainControlsView(mainControlsStage);
        gameOverView = new GameOverView(gameOverStage);
    }

    /**
     * Inits the game world and player
     */
    private void doOnStartGame() {
        gameState = NONE;

        controllers = new HashSet<AbstractController>();
        controllerBuffer = new Stack<AbstractController>();
        controllerRemovalBuffer = new Stack<AbstractController>();

        hudView = new HUDView(hudStage);
        pausedView = new PausedView(pausedStage);
        optionsView = new OptionsView(optionsStage);
        gameView = new GameView(gameStage, joystickStage);

        world = new World(new Vector2(0, 0), false);
        world.setContactListener(new CollisionListener());
        accumulator = 0;

        score = 0;
        enemyAmount = 0;

        startWaitGameOver = 0;

        soundsAndMusic = new SoundsAndMusic();
        startMusic();

        BitmapFont bf = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle(bf, Color.WHITE);
        label = new Label(scoreLabelText, style);
        label.setPosition(hudStage.getWidth() / 6, hudStage.getHeight() - 30);
        label.setFontScale(1.5f);
        hudStage.addActor(label);

        // Generate XML-assets
        MyXMLReader reader = new MyXMLReader();
        generateXMLAssets(reader);

        // Make a ControllerHookup and add PoTDA as a listener
        ControllerHookup controllerHookup = new ControllerHookup(gameStage);
        controllerHookup.addListener(this);

        // Set up ModelBuilder with PhysicsActorFactory and ControllerHookup
        AbstractModelBuilder.setPhysiscActorFactory(new Box2DPhysicsActorFactory(world));
        AbstractModelBuilder.addListener(controllerHookup);

        // Create WaveController
        waveController = new WaveController();

        createPlayer();

        createWorldBorders();

    }

    /**
     * Creates the player
     */
    private void createPlayer() {
        // Add player
        new CharacterBuilder().
                setTeam(Character.PLAYER_TEAM).
                setInventoryFromFile("playerStartInventory.xml").
                setBehaviour(ModelActor.Behaviour.NONE).
                setPosition(new Vector2f(WIDTH_METERS / 2, HEIGHT_METERS / 2)).
                setSprite(Sprites.PLAYER).
                create();

    }

    /**
     * Checks if any enemies are alive
     *
     * @return true if at least one enemy is alive
     */
    private boolean enemiesAlive() {
        return enemyAmount > 0;
    }

    /**
     * Checks if the player is alive
     *
     * @return true if the player's health is above 0
     */
    private boolean playersIsAlive() {
        return hudView.getHealth() > 0;
    }

    /**
     * Creates four obstacles around the playing area
     */
    private void createWorldBorders() {
        final float border_thickness = 0.25f;
        // Bottom
        new ObstacleBuilder().
                setSize(WIDTH_METERS, border_thickness).
                setPosition(new Vector2f(WIDTH_METERS / 2, -border_thickness / 2)).
                setSprite(Sprites.BORDER).
                create();
        // Left
        new ObstacleBuilder().
                setSize(border_thickness, HEIGHT_METERS).
                setPosition(new Vector2f(-border_thickness / 2, HEIGHT_METERS / 2)).
                setSprite(Sprites.BORDER).
                create();
        // Top
        new ObstacleBuilder().
                setSize(WIDTH_METERS, border_thickness).
                setPosition(new Vector2f(WIDTH_METERS / 2, border_thickness / 2 + HEIGHT_METERS)).
                setSprite(Sprites.BORDER).
                create();
        // Right
        new ObstacleBuilder().
                setSize(border_thickness, HEIGHT_METERS).
                setPosition(new Vector2f(border_thickness / 2 + WIDTH_METERS, HEIGHT_METERS / 2)).
                setSprite(Sprites.BORDER).
                create();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, HEIGHT * width / (float) height, HEIGHT);

        hudStage.getViewport().update(width, height, false);
        gameStage.getViewport().update(width, height, false);
        joystickStage.getViewport().update(width, height, false);
        mainMenuStage.getViewport().update(width, height, false);
        optionsStage.getViewport().update(width, height, false);
        pausedStage.getViewport().update(width, height, false);
        mainControlsStage.getViewport().update(width, height, false);
        mainDifficultyStage.getViewport().update(width, height, false);
        gameOverStage.getViewport().update(width, height, false);
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
            case WAITING_FOR_INVENTORY:
                // Update the model
                updateGame();

                // Update the physics world
                doPhysicsStep(Gdx.graphics.getDeltaTime());

                updateWorld(true);

                spawnEnemies();

                if (!enemiesAlive()) {
                    if (waveController.levelFinished() && gameState != WAITING_FOR_INVENTORY) {
                        startWaitInventory = System.currentTimeMillis();
                        gameState = WAITING_FOR_INVENTORY;
                    } else {
                        waveController.quicken(9);  //Passes 10 ms / ms in the level's internal time frame
                    }
                }
                if (gameState.equals(WAITING_FOR_INVENTORY)) {
                    System.out.println("waiting");
                    if ((System.currentTimeMillis() - startWaitInventory) / 1000 < WAITING_TIME_GAME_OVER_SECONDS) {
                        // TODO switch to inventory
                        System.out.println("To inventory");
                        gameState = RUNNING;
                        levelStart();
                    }
                }

                if (!playersIsAlive()) {
                    startWaitGameOver = System.currentTimeMillis();
                    gameState = GAME_OVER;
                    label.setPosition(gameOverStage.getWidth() / 2 - label.getWidth(), gameOverStage.getHeight() * 11 / 16);
                    gameOverStage.addActor(label);
                }
                break;
            case PAUSED:
                // Draw the pause menu
                pausedView.render();
                break;
            case OPTIONS:
                // Draw the options menu
                optionsView.render();
                break;
            case MAIN_MENU:
                // Draw the main menu
                mainMenuView.render();
                break;
            case MAIN_CHOOSE:
                // Draw the choose difficulty menu
                mainDifficultyView.render();
                break;
            case MAIN_CONTROLS:
                // Draw the choose controller menu
                mainControlsView.render();
                break;
            case GAME_OVER:
                final long currentTime = System.currentTimeMillis();
                if ((currentTime - startWaitGameOver) / 1000 >= WAITING_TIME_GAME_OVER_SECONDS) {
                    gameOverView.render();
                } else {
                    updateGame();
                    updateWorld(false);
                    doPhysicsStep(Gdx.graphics.getDeltaTime());
                }
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

    private void levelStart() {
        waveController.newLevel();
    }

    /**
     * Restarts the game by recreating everything
     */
    private void doOnRestartGame() {
        dispose();
        AbstractModelBuilder.clear();
        create();
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

    private void spawnEnemies() {
        List<ScoreChangeListener> scoreChangeListeners = new ArrayList<ScoreChangeListener>();
        scoreChangeListeners.add(this);
        List<EnemyBlueprint> list = waveController.getToSpawn();
        Vector2f playerPosition = Character.player.getPosition();
        for (EnemyBlueprint bp : list) {
            float xx, yy;
            do {
                xx = (float) (WIDTH_METERS * Math.random());
            } while (Math.abs(xx - playerPosition.x) < WIDTH_METERS / (2 * SCALING));

            do {
                yy = (float) (HEIGHT_METERS * Math.random());
            } while (Math.abs(yy - playerPosition.y) < HEIGHT_METERS / (2 * SCALING));

            List<DeathListener> deathListeners = new ArrayList<DeathListener>();
            deathListeners.add(this);
            bp.setListeners(scoreChangeListeners, deathListeners);
            bp.build().setPosition(new Vector2f(xx, yy)).create();


            enemyAmount++;
        }
    }

    /**
     * Updates physics, health bar and renders views
     */

    private void updateWorld(boolean moveCamera) {
        label.setText(scoreLabelText + score);

        // Set the health bar to player's current health
        hudView.setHealthbar(Character.player.getCurrentHealth());

        // Draw the game
        gameView.render(moveCamera);
        hudStage.draw();
        hudView.render();
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
                    if (pausedView.checkIfTouchingPauseResume(vector3)) {
                        // Touching pause resume
                        gameState = RUNNING;
                    } else if (pausedView.checkIfTouchingPauseOptions(vector3)) {
                        // Touching pause options
                        gameState = OPTIONS;
                    } else if (pausedView.checkIfTouchingPauseQuit(vector3)) {
                        // Touching pause quit
                        Gdx.app.exit();
                    }
                    break;
                case OPTIONS:
                    if (optionsView.checkIfTouchingOptionsReturn(vector3)) {
                        // Touched
                        gameState = PAUSED;
                    } else if (optionsView.checkIfTouchingOptionsMusic(vector3)) {
                        soundsAndMusic.setMusicVolume(optionsView.getNewMusicVolume(vector3));
                    } else if (optionsView.checkIfTouchingOptionsSFX(vector3)) {
                        soundsAndMusic.setSFXVolume(optionsView.getNewSFXVolume(vector3));
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
                    if (mainDifficultyView.checkIfTouchingEasy(vector3)) {
                        // TODO Set easy mode
                        doOnStartGame();
                        gameState = RUNNING;
                        Gdx.input.setInputProcessor(joystickStage);
                    } else if (mainDifficultyView.checkIfTouchingHard(vector3)) {
                        // TODO Set hard mode
                        doOnStartGame();
                        gameState = RUNNING;
                        Gdx.input.setInputProcessor(joystickStage);
                    }
                    break;
                case MAIN_CONTROLS:
                    if (mainControlsView.checkIfTouchingTouch(vector3)) {
                        gameState = MAIN_CHOOSE;
                        ControllerOptions.controllerSettings = TOUCH_JOYSTICK;
                        ControllerOptions.joystickStage = joystickStage;
                    } else if (mainControlsView.checkIfTouchingKeyboardOnly(vector3)) {
                        gameState = MAIN_CHOOSE;
                        ControllerOptions.controllerSettings = KEYBOARD_ONLY;
                    } else if (mainControlsView.checkIfTouchingKeyboardMouse(vector3)) {
                        gameState = MAIN_CHOOSE;
                        ControllerOptions.controllerSettings = KEYBOARD_MOUSE;
                    }
                    break;
                case GAME_OVER:
                    if (gameOverView.checkIfTouchingRestartButton(vector3)) {
                        // Restart the game
                        doOnRestartGame();
                    } else if (gameOverView.checkIfTouchingQuitButton(vector3)) {
                        Gdx.app.exit();
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
        mainControlsStage.dispose();
        mainDifficultyStage.dispose();
        mainMenuStage.dispose();
        pausedStage.dispose();
        optionsStage.dispose();
        gameOverStage.dispose();
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

    // XML-asset loading

    private void generateXMLAssets(MyXMLReader reader) {
        generateInventories("inventoryblueprint", reader);
        generateEnemies("enemies", reader);
        generateEnemyGroups("enemygroups", reader);
    }

    private void generateInventories(String path, MyXMLReader reader) {

        FileHandle folder = Gdx.files.internal("inventoryblueprint");

        List<FileHandle> contents = Arrays.asList(folder.list("xml"));
        try {
            for (FileHandle f : contents) {
                InventoryBlueprint.newBlueprint(reader.parseInventory(f));
            }
        } catch (Exception e) {
            throw new Error("failed to generate inventory blueprints: ", e);
        }
    }

    private void generateEnemies(String path, MyXMLReader reader) {
        FileHandle folder = Gdx.files.internal(path);

        List<FileHandle> contents = Arrays.asList(folder.list("xml"));
        try {
            for (FileHandle f : contents) {
                EnemyBlueprint.newBlueprint(reader.parseEnemy(f));
            }
        } catch (Exception e) {
            throw new Error("failed to generate enemy blueprints: ", e);
        }
    }

    private void generateEnemyGroups(String path, MyXMLReader reader) {
        FileHandle folder = Gdx.files.internal(path);

        List<FileHandle> contents = Arrays.asList(folder.list("xml"));
        try {
            for (FileHandle f : contents) {
                EnemyGroup.newGroup(reader.parseEnemyGroup(f));
            }
        } catch (Exception e) {
            throw new Error("failed to generate enemy blueprints: ", e);
        }
    }
}
