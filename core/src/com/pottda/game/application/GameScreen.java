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
import com.pottda.game.view.SoundsAndMusic;

import javax.vecmath.Tuple2f;
import javax.vecmath.Vector2f;

import java.util.ArrayList;
import java.util.List;

import static com.pottda.game.assets.Constants.HEIGHT_METERS;
import static com.pottda.game.assets.Constants.HEIGHT_RATIO;
import static com.pottda.game.assets.Constants.HEIGHT_VIEWPORT;
import static com.pottda.game.assets.Constants.WIDTH_METERS;
import static com.pottda.game.assets.Constants.WIDTH_RATIO;
import static com.pottda.game.assets.Constants.WIDTH_VIEWPORT;

class GameScreen extends AbstractScreen {
    private static final int OBSTACLE_AMOUNT = 10;
    private static final float OBSTACLE_MAX_RADIUS = 3f;
    private static final float OBSTACLE_MIN_RADIUS = 0.5f;
    private static final float SCALING = 2f;
    private static final long WAITING_TIME_GAME_OVER_SECONDS = 3;
    private static final int MAX_ITERATIONS = 1000;
    private static final float OBSTACLE_OFFSET = 1f;

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

        SoundsAndMusic.play();

        createPlayer();

        createWorldBorders();

        createObstacles();
    }

    private void initStages() {

        camera = new OrthographicCamera(WIDTH_METERS / SCALING, HEIGHT_METERS / SCALING);

        backgroundStage = new Stage(new StretchViewport(WIDTH_VIEWPORT, HEIGHT_VIEWPORT));
        hudStage = new HUDStage(new StretchViewport(WIDTH_VIEWPORT, HEIGHT_VIEWPORT));
        modelState.addScoreChangeListener(hudStage);

        Image background = new Image(new Texture(Gdx.files.internal(com.pottda.game.assets.Sprites.MAINBACKGROUND.fileName)));
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

        backgroundStage.getCamera().position.set(new Vector2(camera.position.x / 2 / WIDTH_RATIO, camera.position.y / 2 / HEIGHT_RATIO), 0);
        backgroundStage.draw();
        gameStage.draw();
        if (modelState.getDroppedItems().size() > 0) {
            hudStage.onNewDrop(modelState.getDroppedItems());
            modelState.getDroppedItems().clear();
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
        try {
            gameStage.dispose();
            backgroundStage.dispose();
            hudStage.dispose();
            SoundsAndMusic.dispose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        toPause();
    }

    // Model updating

    private void updateModel(float delta) {
        controllerManager.update(delta);

        doPhysicsStep(delta);

        spawnEnemies(delta);

        if (!modelState.playerAlive()) {
            toGameOver();
        }

        if (!modelState.enemiesAlive() && waveManager.levelFinished()) {
            controllerManager.clearProjectiles();
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
        List<Vector2f> obstaclePositions = new ArrayList<Vector2f>();
        Boolean arenaFull = false;
        for (int i = 0; i < OBSTACLE_AMOUNT && !arenaFull; i++) {
            Boolean legalPlacement;
            int iterationCounter = 0;
            do {
                xx = (float) (Math.random() * (WIDTH_METERS - 2 * (OBSTACLE_OFFSET + OBSTACLE_MAX_RADIUS))) + OBSTACLE_OFFSET + OBSTACLE_MAX_RADIUS;
                yy = (float) (Math.random() * (HEIGHT_METERS - 2 * (OBSTACLE_OFFSET + OBSTACLE_MAX_RADIUS))) + OBSTACLE_OFFSET + OBSTACLE_MAX_RADIUS;
                r = (float) (Math.random() * (OBSTACLE_MAX_RADIUS - OBSTACLE_MIN_RADIUS)) + OBSTACLE_MIN_RADIUS;

                Vector2f position = new Vector2f();
                legalPlacement = true;

                if (obstaclePositions.size() > 0) {
                    for (Tuple2f pos : obstaclePositions) {
                        position.set(xx, yy);
                        position.sub(pos);
                        if (position.length() < 2 * OBSTACLE_MAX_RADIUS + OBSTACLE_OFFSET) {
                            legalPlacement = false;
                            break;
                        }
                    }
                }

                if (legalPlacement) {
                    position.set(xx, yy);
                    obstaclePositions.add(position);
                }
            } while (!legalPlacement && iterationCounter++ < MAX_ITERATIONS);

            if (iterationCounter <= MAX_ITERATIONS && legalPlacement) {
                new ObstacleBuilder().
                        setRadius(r).
                        setPosition(new Vector2f(xx, yy)).
                        setSprite(com.pottda.game.assets.Sprites.BORDER).
                        create();
            } else {
                arenaFull = true;
            }
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
                setInventoryFromFile("sizedItemTestInv.xml").
                setBehaviour(ModelActor.Behaviour.NONE).
                setPosition(new Vector2f(WIDTH_METERS / 2, HEIGHT_METERS / 2)).
                setSprite(com.pottda.game.assets.Sprites.PLAYER).
                create();
    }

    private void createWorldBorders() {
        final float border_thickness = 1f;
        // Bottom border
        new ObstacleBuilder().
                setSize(WIDTH_METERS + border_thickness * 2, border_thickness).
                setPosition(new Vector2f(WIDTH_METERS / 2, -border_thickness / 2)).
                setSprite(com.pottda.game.assets.Sprites.NONE).
                create();
        // Left border
        new ObstacleBuilder().
                setSize(border_thickness, HEIGHT_METERS + border_thickness * 2).
                setPosition(new Vector2f(-border_thickness / 2, HEIGHT_METERS / 2)).
                setSprite(com.pottda.game.assets.Sprites.NONE).
                create();
        // Top border
        new ObstacleBuilder().
                setSize(WIDTH_METERS + border_thickness * 2, border_thickness).
                setPosition(new Vector2f(WIDTH_METERS / 2, border_thickness / 2 + HEIGHT_METERS)).
                setSprite(com.pottda.game.assets.Sprites.NONE).
                create();
        // Right border
        new ObstacleBuilder().
                setSize(border_thickness, HEIGHT_METERS + border_thickness * 2).
                setPosition(new Vector2f(border_thickness / 2 + WIDTH_METERS, HEIGHT_METERS / 2)).
                setSprite(com.pottda.game.assets.Sprites.NONE).
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

            // Expand inventory at levels 2, 4 and every 3rd level
            if (waveManager.getLevel() % 3 == 0 ||
                    waveManager.getLevel() == 2 ||
                    waveManager.getLevel() == 4) {
                modelState.expandPlayerInventory();
            }

            hudStage.showLevelClear(waveManager.getLevel());
            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    switchScreen(new InventoryManagementScreen(game,
                            thisScreen,
                            modelState.getPlayer().getInventory(),
                            modelState.getStorage()));
                    waveManager.newLevel();
                }
            }, 2);
        }
    }

}
