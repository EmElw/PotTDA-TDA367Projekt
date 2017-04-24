package com.pottda.game.Model;

import javax.vecmath.Vector2f;

/**
 * Created by Magnus on 2017-04-24.
 */
public interface ModelActorListener {

    /**
     * Called when a translation occurs in the Actor
     * @param translationVector the translation, of unspecified scale, as a Vector2f
     * @param relative if the translation is relative to the listener's coordinate, or some 0,0 point
     */
    void onTranslate(Vector2f translationVector, boolean relative);

    /**
     * Called when the Actor is returned to the object pool
     */
    void onDeactivate();

    /**
     * Called when a Actor is retrieved from the object pool
     */
    void onActivate();

}
