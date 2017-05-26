package com.pottda.game.model;

/**
 * Created by Magnus on 2017-05-26.
 */
public class ModelState implements DeathListener, ScoreChangeListener, NewModelListener {

    private int score;
    private int enemiesAlive;

    public ModelState() {
        score = 0;
        enemiesAlive = 0;
    }


    public boolean enemiesAlive() {
        return enemiesAlive == 0;
    }

    public int getScore() {
        return score;
    }

    @Override
    public void onDeath() {

    }

    @Override
    public void scoreChanged(int points) {
        score += points;
    }

    @Override
    public void onNewModel(ModelActor m) {
        if (m instanceof Character) {
            if (m.team == ModelActor.ENEMY_TEAM) {
                enemiesAlive++;
            }
            ((Character) m).addDeathListener(this);
        }
    }
}
