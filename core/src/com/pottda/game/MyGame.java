package com.pottda.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pottda.game.controller.AbstractController;

import java.util.List;

public class MyGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture img;
    private Vector2 v = new Vector2(0, 0);
    private Box2DDebugRenderer box2DDebugRenderer;
    private float accumulator;
    private OrthographicCamera camera;
    private Stage stage;

    private List<AbstractController> controllers;
    private World world;

    // PLaying around
    private BodyDef bodyDef;
    private Body body;
    private Shape shape;
    private FixtureDef fixtureDef;
    private Fixture fixture;
    private ShapeRenderer shapeRenderer;
    private Sprite sprite;
    private AbstractController abstractController;

    @Override
    public void create() {
        // Testing
        float aspectRatio = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 10f * aspectRatio, 10f);
        batch = new SpriteBatch();
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera), batch);

        Box2D.init();
        box2DDebugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();
        //img = new Texture(Gdx.files.internal("badlogic.jpg"));
        world = new World(new Vector2(0, 0), false);
        accumulator = 0;

        // Messing around
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(400, 240);

        body = world.createBody(bodyDef);

        shape = new CircleShape();
        shape.setRadius(2.5f);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;

        fixture = body.createFixture(fixtureDef);

        shape.dispose();

        img = new Texture(Gdx.files.internal("CircleTest.png"));
        sprite = new Sprite(img);

        shapeRenderer = new ShapeRenderer();
//
//        if (Gdx.app.getType() == Application.ApplicationType.Android){ // if on android
//            abstractController = new TouchJoystickController(new ArrayList<AttackListener>(), new ArrayList<MovementListener>(), false, stage);
//        } else if (Gdx.app.getType() == Application.ApplicationType.Desktop) { // if on desktop
//            // Check if using mouse?
//            //abstractController = new KeyboardOnlyController(new ArrayList<AttackListener>(), new ArrayList<MovementListener>(), false);
//
//            abstractController = new KeyboardMouseController(new ArrayList<AttackListener>(), new ArrayList<MovementListener>(), false, body);
//        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        // Update all controllers, causing the model to update
        for (AbstractController c : controllers) {
            c.onNewFrame();
        }

        // Update the world
        doPhysicsStep(Gdx.graphics.getDeltaTime());

        // Draw stuff
        batch.begin();
        sprite.draw(batch);
        batch.end();
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
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
