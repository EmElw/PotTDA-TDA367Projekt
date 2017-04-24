package com.pottda.game.Model;


import com.pottda.game.Controller.AbstractController;

import javax.vecmath.Vector2f;
import java.util.List;

/**
 * Created by Gustav Lahti on 2017-04-07.
 */

public abstract class Actor implements PhysicsListener {
    public boolean isProjectile;
    public int team;
    public AbstractController controller;

    private List<SpriteListener> spriteListeners;
    private List<ModelActorListener> modelActorListeners;

    public abstract void collide(Actor other);

    @Override
    public void onNewPosition(final Vector2f position) {

        // TODO, but something along these lines
        for (SpriteListener l : spriteListeners) {
            l.onSpriteUpdate((int) position.x, (int) position.y);
        }
    }

    @Override
    public void onCollision(Actor other) {
    }
}
