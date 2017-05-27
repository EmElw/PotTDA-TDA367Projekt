package com.pottda.game.model;

import java.util.List;

class EnemyDeathListener implements DeathListener{
    private final int points;
    private final List<ScoreChangeListener> scoreChangeListenerList;

    EnemyDeathListener(int points, List<ScoreChangeListener> scoreChangeListenerList){
        this.points = points;
        this.scoreChangeListenerList = scoreChangeListenerList;
    }

    @Override
    public void onDeath(Character character) {
        for (ScoreChangeListener scl : scoreChangeListenerList){
            scl.scoreChanged(points);
        }
    }
}
