package com.pottda.game.model;

/**
 * Created by rikar on 2017-04-07.
 */

public interface ProjectileListener {

    void onAttack(Projectile projectile);

    void onHit();

    void onDestruction();

    void onTimer();
}
