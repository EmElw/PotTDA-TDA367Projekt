package com.pottda.game.physicsBox2D;

import com.badlogic.gdx.physics.box2d.*;
import com.pottda.game.model.Character;
import com.pottda.game.model.Obstacle;
import com.pottda.game.model.Projectile;

public class CollisionListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        if (contact.getFixtureA().isSensor() == contact.getFixtureB().isSensor()) {
            return;
        }

        Body projectileBody;
        Body targetBody;

        if (contact.getFixtureA().isSensor()) {
            projectileBody = contact.getFixtureA().getBody();
            targetBody = contact.getFixtureB().getBody();
        } else {
            projectileBody = contact.getFixtureB().getBody();
            targetBody = contact.getFixtureA().getBody();
        }

        Projectile projectile = projectileBody.getUserData() instanceof Projectile ?
                ((Projectile) projectileBody.getUserData()) : null;
        if (projectile == null) {
            return;
        }

        Character target = targetBody.getUserData() instanceof Character ?
                ((Character) targetBody.getUserData()) : null;
        if (target == null) {
            if (targetBody.getUserData() instanceof Obstacle) {
                projectile.onCollision();
                return;
            }
            return;
        }

        if (target.team == projectile.team) {
            return;
        }

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
