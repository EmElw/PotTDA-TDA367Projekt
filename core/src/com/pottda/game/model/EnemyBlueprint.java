package com.pottda.game.model;

import com.pottda.game.model.builders.CharacterBuilder;
import com.pottda.game.model.builders.IModelBuilder;

import java.lang.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Blueprint-type class for a single enemy ModelActor
 */
public class EnemyBlueprint {

    private final static Map<String, EnemyBlueprint> blueprints = new HashMap<>();

    /**
     * Returns a semi-built enemy
     *
     * @param name the name of the enemy to build
     * @return a {@link IModelBuilder} with behaviour, inventory and sprite set
     */
    public static IModelBuilder getEnemy(String name) {
        return blueprints.get(name).build();
    }

    /**
     * Returns an {@link EnemyBlueprint} for the given name
     *
     * @param s a {@link String}
     * @return an {@link EnemyBlueprint}
     */
    public static EnemyBlueprint getBlueprint(String s) {
        if (blueprints.containsKey(s)) {
            return blueprints.get(s);
        } else {
            throw new Error("no such enemy " + s);
        }
    }

    /**
     * Create and add a blueprint to the library from an {@link XMLEnemy}
     *
     * @param xml an {@link XMLEnemy}
     */
    public static void newBlueprint(XMLEnemy xml) {
        blueprints.put(xml.name, new EnemyBlueprint(xml));
    }

    // ---- meta
    private final String name;

    private final int difficulty;

    // ---- properties
    private final ModelActor.Behaviour behaviour;
    private final String inventoryName;
    private final Sprites sprite;

    private List<DeathListener> deathListeners;

    private final int scoreValue;

    private EnemyBlueprint(XMLEnemy xml) {
        this.name = xml.name;
        this.scoreValue = xml.scoreValue;
        this.difficulty = xml.difficulty;
        this.behaviour = xml.behaviour;
        this.inventoryName = xml.inventoryName;
        this.sprite = xml.sprite;
    }

    public IModelBuilder build() {

        // TODO implement death-listeners for score
        return new CharacterBuilder().
                setTeam(Character.ENEMY_TEAM).
                setBehaviour(behaviour).
                setInventory(InventoryBlueprint.getInventory(inventoryName)).
                setDeathListeners(deathListeners).
                setSprite(sprite);
    }

    public String getName() {
        return name;
    }

    public void setListeners(List<ScoreChangeListener> scoreChangeListeners, List<DeathListener> deathListeners) {
        this.deathListeners = deathListeners;
        deathListeners.add(new EnemyDeathListener(scoreValue, scoreChangeListeners));
    }
}
