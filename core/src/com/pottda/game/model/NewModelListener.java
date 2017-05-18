package com.pottda.game.model;

/**
 * Listener for objects that want to listen to
 * when new model instances are created
 */
public interface NewModelListener {

    void onNewModel(ModelActor m);
}
