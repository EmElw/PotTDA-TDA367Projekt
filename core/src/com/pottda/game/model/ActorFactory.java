package com.pottda.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pottda.game.controller.*;

import java.util.List;

import javax.vecmath.Vector2f;

public abstract class ActorFactory {
    public abstract AbstractController buildEnemy(Stage stage, Texture texture, Vector2f position);

    public abstract AbstractController buildPlayer(Stage stage, Texture texture, Vector2f position);

    public abstract AbstractController buildProjectile(Stage stage, Texture texture, int team, boolean bounces, boolean penetrates, Vector2f position);

    public abstract AbstractController buildObstacle(Stage stage, Texture texture, Vector2f position);
}
