package com.pottda.game.model;

import java.util.List;

/**
 * Created by Magnus on 2017-05-21.
 */
public class XMLEnemyGroup {

    public final List<String> enemies;
    public final int difficulty;

    public XMLEnemyGroup(List<String> enemies, int difficulty) {
        this.enemies = enemies;
        this.difficulty = difficulty;
    }

    // TODO should probably be in application DITO for other XML objects
}
