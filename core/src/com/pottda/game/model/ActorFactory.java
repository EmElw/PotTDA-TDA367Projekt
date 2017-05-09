package com.pottda.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pottda.game.controller.*;
import com.pottda.game.view.*;

import java.util.List;

public abstract class ActorFactory {
    public abstract AbstractController buildEnemy(Stage stage, Texture texture);

    public abstract AbstractController buildPlayer(Stage stage, Texture texture);

    public abstract AbstractController buildProjectile(Stage stage, Texture texture, int team, boolean bounces, boolean penetrates);

    public abstract AbstractController buildObstacle(Stage stage, Texture texture);
}
