package com.pottda.game.pathfindingGDXAI;

import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.physics.box2d.World;

public class CollisionDetector implements RaycastCollisionDetector {
    private World world;

    protected CollisionDetector(World world){
        this.world = world;
    }

    @Override
    public boolean collides(Ray ray) {
        ray.start
        return false;
    }

    @Override
    public boolean findCollision(Collision outputCollision, Ray inputRay) {
        return false;
    }
}
