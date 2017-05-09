package com.pottda.game.model;

import com.pottda.game.controller.AbstractController;

public abstract class ActorFactory {
    public abstract AbstractController buildEnemy();

    public abstract AbstractController buildPlayer();

    public abstract AbstractController buildProjectile(int team, boolean bounces, boolean penetrates);

    public abstract AbstractController buildObstacle();
}
