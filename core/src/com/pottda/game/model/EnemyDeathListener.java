package com.pottda.game.model;

import java.util.List;

public class EnemyDeathListener implements DeathListener{
    private int points;
    private List<ScoreChangeListener> scoreChangeListenerList;

    public EnemyDeathListener(int points, List<ScoreChangeListener> scoreChangeListenerList){
        this.points = points;
        this.scoreChangeListenerList = scoreChangeListenerList;
    }

    @Override
    public void onDeath() {
        for (ScoreChangeListener scl : scoreChangeListenerList){
            scl.scoreChanged(points);
        }
    }
}
