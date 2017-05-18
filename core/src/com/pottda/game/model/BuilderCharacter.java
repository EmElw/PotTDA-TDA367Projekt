package com.pottda.game.model;

import com.pottda.game.view.Sprites;

import javax.vecmath.Vector2f;


/**
 * {@inheritDoc}
 * <p>
 * Specifically, a {@link Character}
 */
public interface BuilderCharacter extends BuilderModel {
    BuilderCharacter setTeam(int n);

    BuilderCharacter setInventory(Inventory inv);

    BuilderCharacter setInventoryFromFile(String xmlFilePath);

    BuilderCharacter setPhysicsActor(PhysicsActor physicsActor);
}
