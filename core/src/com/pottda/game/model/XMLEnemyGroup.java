package com.pottda.game.model;

import java.util.List;

/**
 * Created by Magnus on 2017-05-21.
 */
public class XMLEnemyGroup {

    final List<String> enemies;
    private final int difficulty;
    private final String name;

    public XMLEnemyGroup(String name,List<String> enemies, int difficulty) {
        this.enemies = enemies;
        this.difficulty = difficulty;
        this.name = name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getName() {
        return name;
    }

    // TODO should probably be in application DITO for other XML objects
}
