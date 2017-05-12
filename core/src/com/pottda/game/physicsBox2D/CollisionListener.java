package com.pottda.game.physicsBox2D;

import com.badlogic.gdx.physics.box2d.*;
import com.pottda.game.model.*;
import com.pottda.game.model.Character;

public class CollisionListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        // Continue if one fixture is a sensor and one isn't
        if (contact.getFixtureA().isSensor() == contact.getFixtureB().isSensor()) {
            return;
        }

        Body projectileBody;
        Body targetBody;

        // Set bodies to their corresponding variable, only Projectiles have sensors
        if (contact.getFixtureA().isSensor()) {
            projectileBody = contact.getFixtureA().getBody();
            targetBody = contact.getFixtureB().getBody();
        } else {
            projectileBody = contact.getFixtureB().getBody();
            targetBody = contact.getFixtureA().getBody();
        }

        // Make sure projectile actually IS a Projectile
        Projectile projectile = projectileBody.getUserData() instanceof Projectile ?
                ((Projectile) projectileBody.getUserData()) : null;
        if(projectile == null){
            return;
        }

        // Make sure target is a Character, otherwise check if it's an obstacle,
        // in which case give it to the projectile and let it handle the collision
        Character target = targetBody.getUserData() instanceof Character ?
                ((Character) targetBody.getUserData()) : null;
        if (target == null) {
            Obstacle obstacle = targetBody.getUserData() instanceof Obstacle ?
                    ((Obstacle) targetBody.getUserData()) : null;

            if(obstacle != null){
                projectile.onCollision();
            }
            return;
        }

        // Check if friendly fire or not
        if (target.team == projectile.team) {
            return;
        }

        // Give the Character to the Projectile
        projectile.onCollision(target);
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
