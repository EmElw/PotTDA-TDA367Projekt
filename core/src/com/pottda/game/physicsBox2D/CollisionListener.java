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

        Projectile projectile = projectileBody.getUserData() instanceof Projectile ?
                ((Projectile) projectileBody.getUserData()) : null;
        Character target = targetBody.getUserData() instanceof Character ?
                ((Character) targetBody.getUserData()) : null;

        // If projectile exists, clear its list of things it has damaged
        if (projectile == null){
            return;
        }
        projectile.hasDamaged.clear();

        // Check if target is a Character
        if (target == null){
            return;
        }

        // Check if friendly fire or not
        if (target.team == projectile.team){
            return;
        }

        // Check if projectile has already damaged the target
        if(projectile.hasDamaged.contains(target)){
            return;
        }

        // Deal damage and add target to targets projectile has damaged
        target.takeDamage(projectile.damage);
        projectile.hasDamaged.add(target);
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
