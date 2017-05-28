package com.pottda.game.model;

import com.pottda.game.model.builders.CharacterBuilder;
import com.pottda.game.model.builders.IModelBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Blueprint-type class for a single enemy ModelActor
 */
public class EnemyBlueprint {

    private final static Map<String, EnemyBlueprint> blueprints = new HashMap<String, EnemyBlueprint>();

    /**
     * Returns a semi-built enemy with behaviour, inventory, scoreValue and sprite set
     *
     * @param name the name of the enemy to build
     * @return a {@link IModelBuilder}
     */
    public static IModelBuilder getEnemy(String name) {
        return blueprints.get(name).build();
    }

    static EnemyBlueprint getBlueprint(String s) {
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

    private final String name;

    private final ModelActor.Behaviour behaviour;
    private final String inventoryName;
    private final com.pottda.game.assets.Sprites sprite;

    private final int scoreValue;

    private EnemyBlueprint(XMLEnemy xml) {
        this.name = xml.name;
        this.scoreValue = xml.scoreValue;
        this.behaviour = xml.behaviour;
        this.inventoryName = xml.inventoryName;
        this.sprite = xml.sprite;
    }

    public IModelBuilder build() {

        return new CharacterBuilder().
                setScoreValue(scoreValue).
                setTeam(Character.ENEMY_TEAM).
                setBehaviour(behaviour).
                setInventory(InventoryBlueprint.getInventory(inventoryName)).
                setSprite(sprite);
    }

    public String getName() {
        return name;
    }

}
