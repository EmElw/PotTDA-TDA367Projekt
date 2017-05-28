package com.pottda.game.application;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.pottda.game.controller.ControllerHookup;
import com.pottda.game.controller.ControllerManager;
import com.pottda.game.model.Character;
import com.pottda.game.model.*;
import com.pottda.game.model.builders.AbstractModelBuilder;
import com.pottda.game.model.builders.CharacterBuilder;
import com.pottda.game.model.builders.ObstacleBuilder;
import com.pottda.game.physicsBox2D.Box2DPhysicsActorFactory;
import com.pottda.game.physicsBox2D.CollisionListener;

import javax.vecmath.Vector2f;

import static com.pottda.game.model.Constants.*;

class GameScreen extends AbstractScreen {
    private static final int OBSTACLE_AMOUNT = 10;
    private static final float OBSTACLE_MAX_RADIUS = 3f;
    private static final float OBSTACLE_MIN_RADIUS = 0.5f;
    private static final float SCALING = 2f;
    private static final long WAITING_TIME_GAME_OVER_SECONDS = 3;

    private OrthographicCamera camera;

    private HUDStage hudStage;
    private Stage gameStage;
    private Stage backgroundStage;

    private World world;

    /*
    Stores delta time on new frames until it exceeds the physics world's update time threshold
     */
    private float accumulator;

    private ModelState modelState;
    private WaveManager waveManager;
    private ControllerManager controllerManager;

    GameScreen(Game game) {
        super(game);
        create();
    }

    private void create() {
        modelState = new ModelState();

        initStages();

        world = new World(new Vector2(0, 0), false);
        world.setContactListener(new CollisionListener());
        accumulator = 0;
        controllerManager = new ControllerManager();
        ControllerHookup controllerHookup = new ControllerHookup(gameStage, hudStage);
        controllerHookup.addListener(controllerManager);

        AbstractModelBuilder.clear();
        AbstractModelBuilder.setPhysiscActorFactory(new Box2DPhysicsActorFactory(world));

        AbstractModelBuilder.addListener(controllerHookup);
        AbstractModelBuilder.addListener(modelState);

        waveManager = new WaveManager();

        createPlayer();

        createWorldBorders();

        createObstacles();
    }

    private void initStages() {

        camera = new OrthographicCamera(WIDTH_METERS / SCALING, HEIGHT_METERS / SCALING);

        backgroundStage = new Stage(new StretchViewport(WIDTH_VIEWPORT, HEIGHT_VIEWPORT));
        hudStage = new HUDStage(new StretchViewport(WIDTH_VIEWPORT, HEIGHT_VIEWPORT));
        modelState.addScoreChangeListener(hudStage);

        Image background = new Image(new Texture(Gdx.files.internal(Sprites.MAINBACKGROUND.fileName)));
        background.setPosition(-background.getPrefWidth() / 4, -background.getPrefHeight() / 4);
        backgroundStage.addActor(background);

        gameStage = new Stage(new StretchViewport(WIDTH_METERS / SCALING, HEIGHT_METERS / SCALING, camera));

        Gdx.input.setInputProcessor(hudStage);
    }

    @Override
    public void resize(int width, int height) {
        gameStage.getViewport().update(width, height, false);
        hudStage.getViewport().update(width, height, false);
        backgroundStage.getViewport().update(width, height, false);
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        Gdx.input.setInputProcessor(hudStage);
        updateModel(delta);

        camera.position.set(controllerManager.getPlayerController().getView().getX(),
                controllerManager.getPlayerController().getView().getY(),
                0);

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        backgroundStage.draw();
        gameStage.draw();
        if (modelState.droppedItems.size() > 0) {
            hudStage.onNewDrop(modelState.droppedItems);
            modelState.droppedItems.clear();
        }

        if (hudStage.toPause()) {
            hudStage.setToPause(false);
            toPause();
        }

        hudStage.draw();

        batch.end();

    }

    @Override
    public void dispose() {
        gameStage.dispose();
        backgroundStage.dispose();
        hudStage.dispose();
    }

    @Override
    public void pause() {
        toPause();
    }

    // Model updating

    private void updateModel(float delta) {
        controllerManager.updateControllers();

        doPhysicsStep(delta);

        spawnEnemies(delta);

        if (!modelState.playerAlive()) {
            toGameOver();
        }

        if (!modelState.enemiesAlive() && waveManager.levelFinished()) {
            toInventoryManagement();
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

    // ModelActor creation

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

    private void spawnEnemies(float delta) {
        if (modelState.playerAlive()) {
            Vector2f playerPosition = modelState.getPlayer().getPosition();

            waveManager.progressTime((modelState.enemiesAlive() ? delta : delta * 5));

            for (EnemyBlueprint bp : waveManager.getToSpawn()) {
                float xx, yy;
                do {
                    xx = (float) (WIDTH_METERS * Math.random());
                } while (Math.abs(xx - playerPosition.x) < WIDTH_METERS / (2 * SCALING));

                do {
                    yy = (float) (HEIGHT_METERS * Math.random());
                } while (Math.abs(yy - playerPosition.y) < HEIGHT_METERS / (2 * SCALING));

                bp.build().setPosition(new Vector2f(xx, yy)).create();
            }
        }
    }

    private void createPlayer() {
        new CharacterBuilder().
                setTeam(Character.PLAYER_TEAM).
                setInventoryFromFile("sizedItemTestInv.xml"). //playerStartInventory
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

    // Screen-switching

    private void toGameOver() {
        if (Timer.instance().isEmpty())
            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    switchScreen(new GameOverScreen(game, modelState.getScore()));
                    dispose();
                }
            }, 2);
    }

    private void toPause() {
        switchScreen(new PausedScreen(game, this));
    }

    private void toInventoryManagement() {
        final Screen thisScreen = this;
        if (Timer.instance().isEmpty()) {
            hudStage.showLevelClear(waveManager.getLevel());
            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    switchScreen(new InventoryManagementScreen(game,
                            thisScreen,
                            modelState.getInventory(),
                            modelState.getStorage()));
                    waveManager.newLevel();
                }
            }, 2);
        }
    }

}
