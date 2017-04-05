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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import sun.security.pkcs11.wrapper.Constants;

public class MyGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture img;
    private Vector2 v = new Vector2(0, 0);
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private float accumulator;
    private OrthographicCamera camera;

    // PLaying around
    private BodyDef bodyDef;
    private Body body;
    private Shape shape;
    private FixtureDef fixtureDef;
    private Fixture fixture;
    private ShapeRenderer shapeRenderer;
    private Sprite sprite;

    @Override
    public void create() {
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
    }

    @Override
    public void render() {
        camera.update();

        box2DDebugRenderer.render(world, camera.combined);
        if (Gdx.input.isTouched()) {
            v.x = (Gdx.input.getX() - body.getPosition().x);
            v.y = (Gdx.graphics.getHeight() - Gdx.input.getY() - body.getPosition().y);
        } else {
            v.x = 0;
            v.y = 0;
        }

        body.applyForceToCenter(v, true);

        doPhysicsStep(Gdx.graphics.getDeltaTime());

        sprite.setPosition(body.getPosition().x, body.getPosition().y);

        Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(sprite, sprite.getX(), sprite.getY());
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
