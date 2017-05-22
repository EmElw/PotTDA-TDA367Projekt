package com.pottda.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.XmlReader;
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
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.util.*;

import static com.pottda.game.PoTDA.GameState.*;
import static com.pottda.game.controller.ControllerOptions.ControllerMode.*;

public class PoTDA extends ApplicationAdapter implements NewControllerListener {
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

    @Override
    public void onNewController(AbstractController c) {
        controllerBuffer.add(c);
    }


    public enum GameState {
        NONE,
        RUNNING,
        WAITING,
        PAUSED,
        OPTIONS,
        MAIN_MENU,
        MAIN_CHOOSE,
        MAIN_CONTROLS,
        GAME_OVER
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

    private long startWaitGameOver = 0;
    private static final long WAITING_TIME_GAME_OVER_SECONDS = 3;

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
        pausedView = new PausedView(pausedStage);
        optionsView = new OptionsView(optionsStage);
        gameView = new GameView(gameStage, joystickStage);

        world = new World(new Vector2(0, 0), false);
        world.setContactListener(new CollisionListener());
        accumulator = 0;

        soundsAndMusic = new SoundsAndMusic();
        startMusic();

        generateInventoryBlueprints();

        // Make a ControllerHookup and add PoTDA as a listener
        ControllerHookup controllerHookup = new ControllerHookup(gameStage);
        controllerHookup.addListener(this);

        // Set up ModelBuilder with PhysicsActorFactory and ControllerHookup
        AbstractModelBuilder.setPhysiscActorFactory(new Box2DPhysicsActorFactory(world));
        AbstractModelBuilder.addListener(controllerHookup);

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
//        ActorFactory.get().buildPlayer(com.pottda.game.model.Sprites.NONE,
//                new Vector2f(WIDTH_METERS * scaling / 2, HEIGHT_METERS * scaling / 2));
        new CharacterBuilder().
                setTeam(Character.PLAYER_TEAM).
                setInventoryFromFile("playerStartInventory.xml").
                setBehaviour(ModelActor.Behaviour.NONE).
                setPosition(new Vector2f(WIDTH_METERS * scaling / 2, HEIGHT_METERS * scaling / 2)).
                setSprite(Sprites.PLAYER).
                create();

    }

    /**
     * Checks if any enemies are alive
     *
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
     * Checks if the player is alive
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
        // Scale the area bigger or smaller
        final float area_scaling = 2.5f;
        // Bottom
//        controllers.add(ActorFactory.get().buildObstacle(com.pottda.game.model.Sprites.BORDER,
//                new Vector2f(0, 0), new Vector2f(WIDTH_METERS * area_scaling, border_thickness * HEIGHT_RATIO), true));
        new ObstacleBuilder().
                setSize(WIDTH_METERS * area_scaling, border_thickness).
                setPosition(new Vector2f((WIDTH_METERS * area_scaling) / 2, -border_thickness / 2)).
                setSprite(Sprites.BORDER).
                create();
        // Left
//        controllers.add(ActorFactory.get().buildObstacle(com.pottda.game.model.Sprites.BORDER,
//                new Vector2f(0, 0), new Vector2f(border_thickness * WIDTH_RATIO, HEIGHT_METERS * area_scaling), true));
        new ObstacleBuilder().
                setSize(border_thickness, HEIGHT_METERS * area_scaling).
                setPosition(new Vector2f(-border_thickness / 2, (HEIGHT_METERS * area_scaling) / 2)).
                setSprite(Sprites.BORDER).
                create();
        // Top
//        controllers.add(ActorFactory.get().buildObstacle(com.pottda.game.model.Sprites.BORDER,
//                new Vector2f(0, HEIGHT_METERS * area_scaling), new Vector2f(WIDTH_METERS * area_scaling, border_thickness * HEIGHT_RATIO), true));
        new ObstacleBuilder().
                setSize(WIDTH_METERS * area_scaling, border_thickness).
                setPosition(new Vector2f((WIDTH_METERS * area_scaling) / 2, border_thickness / 2 + (HEIGHT_METERS * area_scaling))).
                setSprite(Sprites.BORDER).
                create();
        // Right
//        controllers.add(ActorFactory.get().buildObstacle(com.pottda.game.model.Sprites.BORDER,
//                new Vector2f(WIDTH_METERS * area_scaling, 0), new Vector2f(border_thickness * WIDTH_RATIO, (HEIGHT_METERS + right_border_extra) * area_scaling), true));
        new ObstacleBuilder().
                setSize(border_thickness, HEIGHT_METERS * area_scaling).
                setPosition(new Vector2f(border_thickness / 2 + (WIDTH_METERS * area_scaling), (HEIGHT_METERS * area_scaling) / 2)).
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
                // Update the model
                updateGame();

                // Update the physics world
                doPhysicsStep(Gdx.graphics.getDeltaTime());

                updateWorld(true);
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
                if (!playersIsAlive()) {
                    startWaitGameOver = System.currentTimeMillis();
                    gameState = GAME_OVER;
                }
                break;
            case WAITING:
                updateGame();
                updateWorld(true);
                // Check if user has waited 5 seconds
                if (waveController.waited()) {
                    gameState = RUNNING;
                    // Start next wave
                    waveController.startWave();
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

    /**
     * Restarts the game by recreating everything
     */
    private void doOnRestartGame() {
        dispose();
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

    /**
     * Updates physics, health bar and renders views
     */
    private void updateWorld(boolean moveCamera) {
        // Update the physics world
        doPhysicsStep(Gdx.graphics.getDeltaTime());

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
                case WAITING:
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

    private void generateInventoryBlueprints() {

        FileHandle folder = Gdx.files.internal("inventoryblueprint");

        List<FileHandle> contents = Arrays.asList(folder.list("xml"));
        try {
            for (FileHandle f : contents) {
                generateBlueprint(f);
            }
        } catch (Exception e) {
            throw new Error();
        }
    }

    private void generateBlueprint(FileHandle file) throws ClassNotFoundException, ParserConfigurationException, InstantiationException, IllegalAccessException, IOException {
        List<XMLItem> xmlItemList = new ArrayList<XMLItem>();
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

        InventoryBlueprint.createBlueprint(file.name(),
                InventoryFactory.createFromXML(xmlItemList, inventory, file.name()));
    }


}
