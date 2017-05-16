package com.pottda.game.model;

import com.pottda.game.controller.AbstractController;
import com.pottda.game.view.Sprites;

import javax.vecmath.Vector2f;

/**
 * A collection of methods to easily instantiate new actors,
 * i.e. the controller, the model and the view
 */
public abstract class ActorFactory {

    /**
     * The concrete implementation used
     */
    private static ActorFactory instance;

    /**
     * Set the concrete implementation used
     */
    public static void setFactory(ActorFactory factory) {
        instance = factory;
    }

    public static ActorFactory get() {
        return instance;
    }

    /**
     * Creates a new actor-clump for an Enemy and adds it to the controller-collection
     *
     * @param sprite    the sprite of the enemy
     * @param position  the position of the enemy
     * @param inventory the inventory of the enemy
     * @return a {@link AbstractController} that handles the new enemy-actor
     */
    public abstract AbstractController buildEnemy(Sprites sprite, Vector2f position, Inventory inventory);

    /**
     * Creates a new actor-clump for a Player with a default inventory
     * and adds it to the controller-collection
     *
     * @param sprite   the sprite of the player
     * @param position the position of the player
     * @return a {@link AbstractController} that handles the new player-actor
     */
    public abstract AbstractController buildPlayer(Sprites sprite, Vector2f position);

    /**
     * Creates a new actor-clump for a projectile and adds it to the controller-collection
     *
     * @param sprite     the sprite of the projectile
     * @param team       the team-affiliation of the projectile (PLAYER = 0, ENEMY = 1)
     * @param bounces    if the projectile bounces on contact or not
     * @param penetrates if the projectile pierces through enemies
     * @param position   the position of the projectile
     * @return a {@link AbstractController} that handles the new projectile-actor
     */
    public abstract AbstractController buildProjectile(Sprites sprite, int team, boolean bounces, boolean penetrates, Vector2f position);

    /**
     * Creates a new actor-clump for an obstacle and adds it to the controller-collection
     *
     * @param sprite   the sprite of the obstacle
     * @param position the position of the obstacle
     * @param size     the size of the obstacle
     * @param isBorder if the obstacle is the border around the game area
     * @return a {@link AbstractController} that handles the new obstacle-actor
     */
    public abstract AbstractController buildObstacle(Sprites sprite, Vector2f position, Vector2f size, boolean isBorder);
}
