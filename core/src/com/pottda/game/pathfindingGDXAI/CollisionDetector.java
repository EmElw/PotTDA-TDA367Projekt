package com.pottda.game.pathfindingGDXAI;

import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.physics.box2d.World;

public class CollisionDetector implements RaycastCollisionDetector {
    private GDXAIGraph graph;

    protected CollisionDetector(GDXAIGraph graph){
        this.graph = graph;
    }

    @Override
    public boolean collides(Ray ray) {
        Vector start = ray.start;
        Vector end = ray.end;

        start.

        return false;
    }

    @Override
    public boolean findCollision(Collision outputCollision, Ray inputRay) {
        return false;
    }
}
