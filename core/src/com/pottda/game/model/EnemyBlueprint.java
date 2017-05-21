package com.pottda.game.model;

import com.pottda.game.model.builders.CharacterBuilder;
import com.pottda.game.model.builders.IModelBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Magnus on 2017-05-21.
 */
public class EnemyBlueprint {

    private final static Map<String, EnemyBlueprint> blueprintsMap = new HashMap<String, EnemyBlueprint>();

    public static IModelBuilder getEnemy(String name) {
        return blueprintsMap.get(name).build();
    }


    // ---- meta

    private final String name;
    private final int difficulty;

    // ---- properties

    private final ModelActor.Behaviour behaviour;
    private final String inventoryName;
    private final Sprites sprite;
    private final int scoreValue;

    public EnemyBlueprint(XMLEnemy xml) {
        this.name = xml.name;
        this.scoreValue = xml.scoreValue;
        this.difficulty = xml.difficulty;
        this.behaviour = xml.behaviour;
        this.inventoryName = xml.inventoryName;
        this.sprite = Sprites.forName(xml.spriteEnum);
    }

    private IModelBuilder build() {

        // TODO implement death-listeners for score
        return new CharacterBuilder().
                setBehaviour(behaviour).
                setInventory(InventoryBlueprint.getInventory(inventoryName)).
                setSprite(sprite);
    }
}
