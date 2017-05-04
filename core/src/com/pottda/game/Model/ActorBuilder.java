package com.pottda.game.model;

import java.util.Stack;

/**
 * Builder class for creating and reusing actors.
 * All creation of Actors should be done through this class, using the appropiate build function
 * <p>
 * The class stores active and inactive actors, reusing them for performance on mobile systems
 * where the garbage handling can produce significant slow down
 * <p>
 * <p>
 * When an actor is removed, it should:
 * - remove itself from its associated controller, if any
 * - set its associated physics body to sleep
 * - call ActorBuilder.deactivateActor(this)
 * <p>
 * To get a new actor, call appropriate getX function and then apply listeners to controller as necessary
 */
public class ActorBuilder {

    private static Stack<Character> inactiveCharacters = new Stack<Character>();
    private static Stack<Projectile> inactiveProjectiles = new Stack<Projectile>();
    private static Stack<Obstacle> inactiveObstacles = new Stack<Obstacle>();

    public static ModelActor deactivateActor(ModelActor a) {
        if (a instanceof Character) {
            return inactiveCharacters.push((Character) a);
        } else if (a instanceof Projectile) {
            return inactiveProjectiles.push((Projectile) a);
        } else if (a instanceof Obstacle) {
            return inactiveObstacles.push((Obstacle) a);
        } else throw new IllegalArgumentException("Dynamic type of given actor was dud");
    }

    public static Character getCharacter() {

        return inactiveCharacters.isEmpty() ?
                new Character(new Inventory(), 1) :
                inactiveCharacters.pop();
    }

    public static Projectile getProjectile() {
        return inactiveProjectiles.isEmpty() ?
                new Projectile() :
                inactiveProjectiles.pop();
    }

    public static Obstacle getObstacle() {
        return inactiveObstacles.isEmpty() ?
                new Obstacle() :
                inactiveObstacles.pop();
    }
}
