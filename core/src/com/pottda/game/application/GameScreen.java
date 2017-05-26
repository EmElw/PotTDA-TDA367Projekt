package com.pottda.game.application;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.pottda.game.controller.*;
import com.pottda.game.model.*;
import com.pottda.game.model.Character;
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

import static com.pottda.game.model.Constants.HEIGHT_VIEWPORT;
import static com.pottda.game.model.Constants.HEIGHT_METERS;
import static com.pottda.game.model.Constants.WIDTH_VIEWPORT;
import static com.pottda.game.model.Constants.WIDTH_METERS;

class GameScreen extends AbstractScreen implements {
    private static final int OBSTACLE_AMOUNT = 10;
    private static final float OBSTACLE_MAX_RADIUS = 3f;
    private static final float OBSTACLE_MIN_RADIUS = 0.5f;
    private static final long WAITING_TIME_GAME_OVER_SECONDS = 3;

    private Stage hudStage;
    private Stage joystickStage;
    private Stage gameStage;
    private Stage bgStage;

    private World world;

    private float accumulator;

    private HUDView hudView;
    private SoundsAndMusic soundsAndMusic;
    private GameView gameView;
    private BackgroundView backgroundView;

    private ModelState modelState;

    private WaveController waveController;


    private static int score;
    private int enemyAmount;

    private static final float SCALING = 2f;
    private ControllerManager controllerManager;

    GameScreen(Game game) {
        super(game);
        create();
    }

    private long startWaitInventory;

    private void create() {

        world = new World(new Vector2(0, 0), false);
        world.setContactListener(new CollisionListener());
        accumulator = 0;

        score = 0;
        enemyAmount = 0;

        MyXMLReader reader = new MyXMLReader();
        reader.generateXMLAssets();

        controllerManager = new ControllerManager();
        ControllerHookup controllerHookup = new ControllerHookup(gameStage);
        controllerHookup.addListener(controllerManager);

        modelState = new ModelState();

        AbstractModelBuilder.clear();   // Clear any previous configs (TODO improve?)
        AbstractModelBuilder.setPhysiscActorFactory(new Box2DPhysicsActorFactory(world));

        AbstractModelBuilder.addListener(controllerHookup);
        AbstractModelBuilder.addListener(modelState);
        
        waveController = new WaveController();

        createPlayer();

        createWorldBorders();

        createObstacles();
    }


    @Override
    public void resize(int width, int height) {
        hudStage.getViewport().update(width, height, false);
        gameStage.getViewport().update(width, height, false);
        joystickStage.getViewport().update(width, height, false);
        bgStage.getViewport().update(width, height, false);
    }


    @Override
    public void render(SpriteBatch batch, float delta) {


        updateModel(delta);
    }

    private void updateModel(float delta) {
        controllerManager.update();
        doPhysicsStep(delta);

        spawnEnemies();

        if (!enemiesAlive()) {
            if (waveController.levelFinished()) {
                toInventoryManagement();
            } else {
                waveController.quicken((long) (delta * 100));
            }
        }

        if (!playersIsAlive()) {
            toGameOver();
        }

    }

    private void toInventoryManagement() {

    }

    private void toGameOver() {
        switchScreen(new GameOverScreen(game, score));
        dispose();
    }

    @Override
    public void dispose() {
    }


    private void createObstacles() {
        float xx;
        float yy;
        float r;
        for (int i = 0; i < OBSTACLE_AMOUNT; i++) {
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


    private void spawnEnemies() {
        List<ScoreChangeListener> scoreChangeListeners = new ArrayList<ScoreChangeListener>();
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


    private void doPhysicsStep(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= (1.0f / 60.0f)) {
            world.step(1.0f / 60.0f, 6, 2);
            accumulator -= 1.0f / 60.0f;
        }
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
        final float border_thickness = 1f;
        // Bottom border
        new ObstacleBuilder().
                setSize(WIDTH_METERS + border_thickness * 2, border_thickness).
                setPosition(new Vector2f(WIDTH_METERS / 2, -border_thickness / 2)).
                setSprite(Sprites.NONE).
                create();
        // Left border
        new ObstacleBuilder().
                setSize(border_thickness, HEIGHT_METERS + border_thickness * 2).
                setPosition(new Vector2f(-border_thickness / 2, HEIGHT_METERS / 2)).
                setSprite(Sprites.NONE).
                create();
        // Top border
        new ObstacleBuilder().
                setSize(WIDTH_METERS + border_thickness * 2, border_thickness).
                setPosition(new Vector2f(WIDTH_METERS / 2, border_thickness / 2 + HEIGHT_METERS)).
                setSprite(Sprites.NONE).
                create();
        // Right border
        new ObstacleBuilder().
                setSize(border_thickness, HEIGHT_METERS + border_thickness * 2).
                setPosition(new Vector2f(border_thickness / 2 + WIDTH_METERS, HEIGHT_METERS / 2)).
                setSprite(Sprites.NONE).
                create();
    }
}
