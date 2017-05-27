package com.pottda.game.model;

import java.util.List;
import java.util.Set;

class EnemyDeathListener implements DeathListener{
    private final int points;
    private final List<ScoreChangeListener> scoreChangeListenerList;

    EnemyDeathListener(int points, List<ScoreChangeListener> scoreChangeListenerList){
        this.points = points;
        this.scoreChangeListenerList = scoreChangeListenerList;
    }

    @Override
    public void onDeath(Set<Item> inventory) {
        for (ScoreChangeListener scl : scoreChangeListenerList){
            scl.scoreChanged(points);
        }
    }
}
