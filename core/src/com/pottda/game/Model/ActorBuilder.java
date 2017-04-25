package com.pottda.game.Model;

import com.pottda.game.Controller.AbstractController;

import java.util.List;
import java.util.Stack;

/**
 * Builder class for creating and reusing actors.
 * All creation of Actors should be done through this class, using the appropiate build function
 * <p>
 * // TODO do we need active?
 * The class stores active and inactive actors, reusing them for performance on mobile systems
 * where the garbage handling can produce significant slow down
 * <p>
 * <p>
 * When ant actor is removed, it should:
 * - remove itself from its associated controller, if any
 * - set its associated physics body to sleep
 * - call ActorBuilder.deactivateActor(this)
 */
public class ActorBuilder {

    private static Stack<Character> inactiveCharacters;
    private static Stack<Projectile> inactiveProjectiles;
    private static Stack<Obstacle> inactiveObstacles;


    public static void deactivateActor(Character c) {
        inactiveCharacters.push(c);
    }

    public static void deactivateActor(Projectile p) {
        inactiveProjectiles.push(p);
    }

    public static void deactivateActor(Obstacle o) {
        inactiveObstacles.push(o);
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
