package com.pottda.game.pathfindingGDXAI;

import com.pottda.game.model.ModelActor;
import com.pottda.game.model.Pathfinder;

import javax.vecmath.Vector2f;

public class GDXAIPathfinder implements Pathfinder {
    private final GDXAIGraph graph;

    public GDXAIPathfinder(ModelActor goal, int worldWidth, int worldHeight) {
        this.goal = goal;
        graph = new GDXAIGraph(worldWidth, worldHeight);
    }

    public ModelActor goal;

    @Override
    public Vector2f getPath(Vector2f location) {

        return null;
    }

    @Override
    public void setObstacle(Vector2f obstacle) {
        int x = (int)obstacle.x;
        int y = (int)obstacle.y;

        // Assumes objects are not much larger than a grid 'tile'

        boolean alignedX = Math.abs(obstacle.x - x) < 0.2f;
        boolean alignedY = Math.abs(obstacle.y - y) < 0.2f;

        graph.setObstacle(x, y);

        if (!alignedX){
            graph.setObstacle(x + 1, y);
            if(!alignedY){
                graph.setObstacle(x + 1, y + 1);
            }
        }
        if (!alignedY){
            graph.setObstacle(x, y + 1);
        }
    }

    @Override
    public void setGoal(ModelActor goal) {
        this.goal = goal;
    }
}
