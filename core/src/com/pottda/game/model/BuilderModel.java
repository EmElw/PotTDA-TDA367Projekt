package com.pottda.game.model;


import com.pottda.game.view.Sprites;

import javax.vecmath.Vector2f;

/**
 * Interface for a Builder pattern that instantiates a ModelActor
 */
public interface BuilderModel {
    BuilderModel setSprite(Sprites sprites);

    BuilderModel setPosition(Vector2f position);

    ModelActor create();
}
