package com.pottda.game.model.builders;

import com.pottda.game.model.Character;
import com.pottda.game.model.Inventory;
import com.pottda.game.model.ModelActor;

/**
 * Concrete implementation of a Character Builder pattern
 */
public class CharacterBuilder extends AbstractModelBuilder implements ICharacterBuilder {
    private int team = 0;
    private Inventory inventory;

    @Override
    public ModelActor create() {
        Character character = new Character();

        character.team = team;
        character.inventory = inventory;

        character.setPhysicsActor(physiscActorFactory.getCharacterPhysicsActor(character));

        setCommonAndNotify(character);
        return character;
    }

    // --------- Setters

    @Override
    public ICharacterBuilder setTeam(int n) {
        this.team = n;
        return this;
    }

    @Override
    public ICharacterBuilder setInventory(Inventory inv) {
        this.inventory = inv;
        return this;
    }

    @Override
    public ICharacterBuilder setInventoryFromFile(String xmlFilePath) {
        // TODO
        return this;
    }

}
