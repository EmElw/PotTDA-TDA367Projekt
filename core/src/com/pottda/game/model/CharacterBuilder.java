package com.pottda.game.model;

/**
 * Concrete implementation of a Character Builder pattern
 */
public class CharacterBuilder extends AbstractModelBuilder implements BuilderCharacter {
    private int team = 0;
    private Inventory inventory;

    @Override
    public ModelActor create() {
        Character character = new Character(
                null // TODO
        );

        character.team = team;
        character.inventory = inventory;

        setCommonParameters(character);
        return character;
    }

    // --------- Setters

    @Override
    public BuilderCharacter setTeam(int n) {
        this.team = n;
        return this;
    }

    @Override
    public BuilderCharacter setInventory(Inventory inv) {
        this.inventory = inv;
        return this;
    }

    @Override
    public BuilderCharacter setInventoryFromFile(String xmlFilePath) {
        // TODO
        return this;
    }

}
