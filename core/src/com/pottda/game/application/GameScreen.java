package com.pottda.game.application;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.pottda.game.controller.AbstractController;
import com.pottda.game.controller.ControllerHookup;
import com.pottda.game.controller.NewControllerListener;
import com.pottda.game.model.Character;
import com.pottda.game.model.DeathListener;
import com.pottda.game.model.EnemyBlueprint;
import com.pottda.game.model.ModelActor;
import com.pottda.game.model.ScoreChangeListener;
import com.pottda.game.model.Sprites;
import com.pottda.game.model.WaveController;
import com.pottda.game.model.builders.AbstractModelBuilder;
import com.pottda.game.model.builders.CharacterBuilder;
import com.pottda.game.model.builders.ObstacleBuilder;
import com.pottda.game.physicsBox2D.Box2DPhysicsActorFactory;
import com.pottda.game.physicsBox2D.CollisionListener;
import com.pottda.game.view.BackgroundView;
import com.pottda.game.view.GameOverView;
import com.pottda.game.view.GameView;
import com.pottda.game.view.HUDView;
import com.pottda.game.view.SoundsAndMusic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.vecmath.Vector2f;

import static com.pottda.game.application.GameState.GAME_OVER;
import static com.pottda.game.application.GameState.NONE;
import static com.pottda.game.application.GameState.PAUSED;
import static com.pottda.game.application.GameState.RESTARTING;
import static com.pottda.game.application.GameState.RUNNING;
import static com.pottda.game.application.GameState.WAITING_FOR_INVENTORY;
import static com.pottda.game.application.GameState.gameState;
import static com.pottda.game.model.Constants.HEIGHT_VIEWPORT;
import static com.pottda.game.model.Constants.HEIGHT_METERS;
import static com.pottda.game.model.Constants.WIDTH_VIEWPORT;
import static com.pottda.game.model.Constants.WIDTH_METERS;

class GameScreen implements NewControllerListener, ScoreChangeListener, DeathListener {
    private static final int OBSTACLE_AMOUNT = 10;
    private static final float OBSTACLE_MAX_RADIUS = 3f;
    private static final float OBSTACLE_MIN_RADIUS = 0.5f;
    private Stage hudStage;
    private Stage joystickStage;
    private Stage gameStage;
    private Stage gameOverStage;
    private Stage bgStage;

    private World world;

    private float accumulator;

    private HUDView hudView;
    private SoundsAndMusic soundsAndMusic;
    private GameView gameView;
    private BackgroundView backgroundView;

    private GameOverView gameOverView;

    private WaveController waveController;

    private Set<AbstractController> controllers;
    private Stack<AbstractController> controllerBuffer;
    private Stack<AbstractController> controllerRemovalBuffer;

    private long startWaitGameOver;
    private static final long WAITING_TIME_GAME_OVER_SECONDS = 3;

    private static int score;
    private int enemyAmount;

    private Label label;
    private static final String scoreLabelText = "Score: ";

    private static final float SCALING = 2f;

    GameScreen() {
        create();
    }

    private long startWaitInventory;

    private void create() {
        hudStage = new Stage(new StretchViewport(WIDTH_VIEWPORT, HEIGHT_VIEWPORT));
        joystickStage = new Stage(new StretchViewport(WIDTH_VIEWPORT, HEIGHT_VIEWPORT));
        gameStage = new Stage(new StretchViewport(WIDTH_METERS / SCALING, HEIGHT_METERS / SCALING));
        gameStage.getCamera().position.x = WIDTH_METERS / 2 / SCALING;
        gameStage.getCamera().position.y = HEIGHT_METERS / 2 / SCALING;
        gameOverStage = new Stage(new StretchViewport(WIDTH_VIEWPORT, HEIGHT_VIEWPORT));
        bgStage = new Stage(new StretchViewport(WIDTH_METERS, HEIGHT_METERS));
        gameOverView = new GameOverView(gameOverStage);

        soundsAndMusic = new SoundsAndMusic();
    }

    void render() {
        switch (gameState) {
            case RUNNING:
            case WAITING_FOR_INVENTORY:
                updateGame();

                doPhysicsStep(Gdx.graphics.getDeltaTime());

                updateWorld(true);

                spawnEnemies();

                if (!enemiesAlive()) {
                    if (waveController.levelFinished() && gameState != WAITING_FOR_INVENTORY) {
                        startWaitInventory = System.currentTimeMillis();
                        gameState = WAITING_FOR_INVENTORY;
                    } else {
                        waveController.quicken(9);
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

        checkTouch();
    }

    void resize(int width, int height) {
        hudStage.getViewport().update(width, height, false);
        gameStage.getViewport().update(width, height, false);
        joystickStage.getViewport().update(width, height, false);
        gameOverStage.getViewport().update(width, height, false);
        bgStage.getViewport().update(width, height, false);
    }

    void dispose() {
        hudStage.dispose();
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

    void doOnStartGame() {
        gameState = NONE;

        controllers = new HashSet<AbstractController>();
        controllerBuffer = new Stack<AbstractController>();
        controllerRemovalBuffer = new Stack<AbstractController>();

        hudView = new HUDView(hudStage);
        gameView = new GameView(gameStage, joystickStage);
        backgroundView = new BackgroundView(bgStage);

        world = new World(new Vector2(0, 0), false);
        world.setContactListener(new CollisionListener());
        accumulator = 0;

        score = 0;
        enemyAmount = 0;

        startWaitGameOver = 0;

        startMusic();

        score = 0;
        BitmapFont bf = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle(bf, Color.WHITE);
        label = new Label(scoreLabelText, style);
        label.setPosition(hudStage.getWidth() / 6, hudStage.getHeight() - 30);
        label.setFontScale(1.5f);
        hudStage.addActor(label);

        MyXMLReader reader = new MyXMLReader();
        reader.generateXMLAssets();

        ControllerHookup controllerHookup = new ControllerHookup(gameStage);
        controllerHookup.addListener(this);

        AbstractModelBuilder.setPhysiscActorFactory(new Box2DPhysicsActorFactory(world));
        AbstractModelBuilder.addListener(controllerHookup);

        waveController = new WaveController();

        createPlayer();

        createWorldBorders();
    }

    private void createObstacles() {
        float xx;
        float yy;
        float r;
        for (int i = 0; i < OBSTACLE_AMOUNT; i++){
            xx = (float) Math.random() * WIDTH_METERS;
            yy = (float) Math.random() * HEIGHT_METERS;
            r = (float) (Math.random() * (OBSTACLE_MAX_RADIUS - OBSTACLE_MIN_RADIUS)) + OBSTACLE_MIN_RADIUS;

            new ObstacleBuilder().
                    setRadius(r).
                    setPosition(new Vector2f(xx, yy)).
                    setSprite(Sprites.BORDER).
                    create();
        }
    }

    private void levelStart() {
        waveController.newLevel();
    }

    private void updateGame() {
        if (controllers != null) {
            removeDeadActors();

            for (AbstractController c : controllers) {
                c.onNewFrame();
            }
            // TODO could potentially do some juggling to also execute the onFrame for all o these
            controllers.addAll(controllerBuffer);
            controllerBuffer.clear();
        }
    }

    private void updateWorld(boolean moveCamera) {
        label.setText(scoreLabelText + score);

        hudView.setHealthbar(Character.player.getCurrentHealth(), Character.player.getMaxHealth());

        backgroundView.render(gameStage.getCamera());
        gameView.render(moveCamera);
        hudStage.draw();
        hudView.render();
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

    private void prepareForRemoval(AbstractController controller) {
        controller.getModel().getPhysicsActor().destroyBody();
        controller.getView().remove();
        controllerRemovalBuffer.add(controller);
    }

    private void removeDeadActors() {
        for (AbstractController c : controllers) {
            if (c.shouldBeRemoved()) {
                prepareForRemoval(c);
            }
        }

        if (controllerRemovalBuffer.size() > 0) {
            controllers.removeAll(controllerRemovalBuffer);
            controllerRemovalBuffer.clear();
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

    // TODO move to a controller class
    private void checkTouch() {
        // Only check first touch
        if (Gdx.input.justTouched()) {
            Vector3 vector3 = hudStage.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            switch (gameState) {
                case RUNNING:
                    if (hudView.checkIfTouchingPauseButton(vector3)) {
                        gameState = PAUSED;
                    }
                    break;
                case GAME_OVER:
                    if (gameOverView.checkIfTouchingRestartButton(vector3)) {
                        gameState = RESTARTING;
                    } else if (gameOverView.checkIfTouchingQuitButton(vector3)) {
                        Gdx.app.exit();
                    }
                    break;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            switch (gameState) {
                case RUNNING:
                    gameState = PAUSED;
                    break;
            }
        }
    }

    private boolean enemiesAlive() {
        return enemyAmount > 0;
    }

    private boolean playersIsAlive() {
        return hudView.getHealth() > 0;
    }

    private void createPlayer() {
        new CharacterBuilder().
                setTeam(Character.PLAYER_TEAM).
                setInventoryFromFile("playerStartInventory.xml").
                setBehaviour(ModelActor.Behaviour.NONE).
                setPosition(new Vector2f(WIDTH_METERS / 2, HEIGHT_METERS / 2)).
                setSprite(Sprites.PLAYER).
                create();
    }

    private void createWorldBorders() {
        final float border_thickness = 0.25f;
        // Bottom border
        new ObstacleBuilder().
                setSize(WIDTH_METERS, border_thickness).
                setPosition(new Vector2f(WIDTH_METERS / 2, -border_thickness / 2)).
                setSprite(Sprites.BORDER).
                create();
        // Left border
        new ObstacleBuilder().
                setSize(border_thickness, HEIGHT_METERS).
                setPosition(new Vector2f(-border_thickness / 2, HEIGHT_METERS / 2)).
                setSprite(Sprites.BORDER).
                create();
        // Top border
        new ObstacleBuilder().
                setSize(WIDTH_METERS, border_thickness).
                setPosition(new Vector2f(WIDTH_METERS / 2, border_thickness / 2 + HEIGHT_METERS)).
                setSprite(Sprites.BORDER).
                create();
        // Right border
        new ObstacleBuilder().
                setSize(border_thickness, HEIGHT_METERS).
                setPosition(new Vector2f(border_thickness / 2 + WIDTH_METERS, HEIGHT_METERS / 2)).
                setSprite(Sprites.BORDER).
                create();
    }

    Stage getJoystickStage() {
        return joystickStage;
    }

    SoundsAndMusic getSoundsAndMusic() {
        return soundsAndMusic;
    }
}
