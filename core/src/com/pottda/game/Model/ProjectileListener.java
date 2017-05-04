package com.pottda.game.model;
/**
 * Created by rikar on 2017-04-07.
 */

public interface ProjectileListener {

    void onAttack();

    void onHit();

    void onDestruction();

    void onTimer();

    void setTimer(int timer, boolean bool);

    void onDistanceTraveled(int distance, boolean bool);
}
