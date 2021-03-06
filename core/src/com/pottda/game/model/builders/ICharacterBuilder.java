package com.pottda.game.model.builders;

import com.pottda.game.model.Character;
import com.pottda.game.model.DeathListener;
import com.pottda.game.model.Inventory;
import com.pottda.game.model.ModelActor;

import java.util.List;


/**
 * {@inheritDoc}
 * <p>
 * Specifically, a {@link Character}
 */
public interface ICharacterBuilder extends IModelBuilder {
    ICharacterBuilder setTeam(int n);

    ICharacterBuilder setInventory(Inventory inv);

    ICharacterBuilder setInventoryFromFile(String xmlFile);

    ICharacterBuilder setBehaviour(ModelActor.Behaviour behaviour);

    ICharacterBuilder setScoreValue(int scoreValue);

    ICharacterBuilder setDeathListeners(List<DeathListener> deathListeners);
}
