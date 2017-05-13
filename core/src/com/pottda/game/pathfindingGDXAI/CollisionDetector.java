package com.pottda.game.pathfindingGDXAI;

import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.pottda.game.MyGame;

public class CollisionDetector implements RaycastCollisionDetector {
    private GDXAIGraph graph;

    protected CollisionDetector(GDXAIGraph graph) {
        this.graph = graph;
    }

    @Override
    public boolean collides(Ray ray) {
        Vector2 start = (Vector2) ray.start;
        Vector2 end = (Vector2) ray.end;

        float dx = end.x - start.x;
        float dy = end.y - start.y;
        float length = (float) Math.sqrt((double) dx * dx + (double) dy * dy);

        for (float i = 0; i < length &&
                Math.round(start.x + i * dx) >= 0 &&
                Math.round(start.x + i * dx) <= MyGame.WIDTH_METERS &&
                Math.round(start.y + i * dy) >= 0 &&
                Math.round(start.y + i * dy) <= MyGame.HEIGHT_METERS;
             i++){
            if(graph.isObstacle(Math.round(start.x + i * dx), Math.round(start.y + i * dy))){
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean findCollision(Collision outputCollision, Ray inputRay) {
        Vector2 start = (Vector2) inputRay.start;
        Vector2 end = (Vector2) inputRay.end;

        float dx = end.x - start.x;
        float dy = end.y - start.y;
        float length = (float) Math.sqrt((double) dx * dx + (double) dy * dy);

        for (float i = 0; i < length; i++){
            if(graph.isObstacle(Math.round(start.x + i * dx), Math.round(start.y + i * dy))){
                try {
                    outputCollision = new Collision(new Vector2(start.x + i * dx, start.y + i * dy), new Vector2(dx, dy));
                } catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }
}
