package com.pottda.game.model.builders;

import com.pottda.game.model.Character;
import com.pottda.game.model.Inventory;


/**
 * {@inheritDoc}
 * <p>
 * Specifically, a {@link Character}
 */
public interface ICharacterBuilder extends IModelBuilder {
    ICharacterBuilder setTeam(int n);

    ICharacterBuilder setInventory(Inventory inv);

    ICharacterBuilder setInventoryFromFile(String xmlFilePath);
}
