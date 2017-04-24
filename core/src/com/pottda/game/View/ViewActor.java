package com.pottda.game.View;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.pottda.game.Model.SpriteListener;


/**
 *
 */
public class ViewActor extends Actor implements SpriteListener {


    @Override
    public void onSpriteUpdate(float x, float y) {
        this.setX(x);
        this.setY(y);
    }
}
