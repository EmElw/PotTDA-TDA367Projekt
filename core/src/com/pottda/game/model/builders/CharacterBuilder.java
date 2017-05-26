package com.pottda.game.model.builders;

import com.pottda.game.model.*;
import com.pottda.game.model.Character;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of a Character Builder pattern
 */
public class CharacterBuilder extends AbstractModelBuilder implements ICharacterBuilder {
    private int team = 0;
    private Inventory inventory;
    private ModelActor.Behaviour behaviour = null;
    private List<DeathListener> deathListeners;

    @Override
    public ModelActor create() {
        Character character = new Character();

        character.team = team;
        if (team == Character.PLAYER_TEAM) {
            Character.player = character;
        }
        character.inventory = inventory;

        inventory.addInventoryChangeListener(character);

        inventory.compile();

        character.setPhysicsActor(physiscActorFactory.getCharacterPhysicsActor(character));

        character.behaviour = behaviour;

        if(deathListeners != null) {
            character.setDeathListeners(deathListeners);
        }

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
    public ICharacterBuilder setInventoryFromFile(String xmlFile) {
        this.inventory = InventoryBlueprint.getInventory(xmlFile);
        return this;
    }

    @Override
    public ICharacterBuilder setBehaviour(ModelActor.Behaviour behaviour) {
        this.behaviour = behaviour;
        return this;
    }

    @Override
    public ICharacterBuilder setDeathListeners(List<DeathListener> deathListeners) {
        this.deathListeners = deathListeners;
        return this;
    }
}
