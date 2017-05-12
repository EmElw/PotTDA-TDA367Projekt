package com.pottda.game.model;

import com.pottda.game.View.Sprites;
import com.pottda.game.controller.*;

import javax.vecmath.Vector2f;

public abstract class ActorFactory {

    private static ActorFactory instance;

    public static void setFactory(ActorFactory factory) {
        instance = factory;
    }

    public static ActorFactory get() {
        return instance;
    }


    public abstract AbstractController buildEnemy(Sprites sprite, Vector2f position, Inventory inventory);

    public abstract AbstractController buildPlayer(Sprites sprite, Vector2f position);

    public abstract AbstractController buildProjectile(Sprites sprite, int team, boolean bounces, boolean penetrates, Vector2f position);

    public abstract AbstractController buildObstacle(Sprites sprite, Vector2f position, Vector2f size);
}
