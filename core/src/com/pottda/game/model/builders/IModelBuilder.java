package com.pottda.game.model.builders;


import com.pottda.game.model.ModelActor;
import com.pottda.game.model.Sprites;

import javax.vecmath.Vector2f;

/**
 * Interface for a Builder pattern that instantiates a ModelActor
 */
public interface IModelBuilder {
    IModelBuilder setSprite(Sprites sprites);

    IModelBuilder setPosition(Vector2f position);

    ModelActor create();
}
