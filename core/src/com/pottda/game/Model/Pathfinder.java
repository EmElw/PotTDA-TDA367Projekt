package com.pottda.game.model;

import javax.vecmath.Vector2f;

public interface Pathfinder {
    Vector2f getPath(Vector2f location);

    void setObstacle(Vector2f obstacle);

    void setGoal(ModelActor goal);
}
