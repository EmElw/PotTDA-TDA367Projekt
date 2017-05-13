package com.pottda.game.pathfindingGDXAI;

import com.badlogic.gdx.ai.pfa.*;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.SortedIntList;
import com.pottda.game.model.ModelActor;
import com.pottda.game.model.Pathfinder;

import javax.vecmath.Vector2f;
import java.util.List;

public class GDXAIPathfinder implements Pathfinder {
    private final GDXAIGraph graph;
    public static ModelActor goal = null;
    private IndexedAStarPathFinder indexedAStarPathFinder;
    private CollisionDetector collisionDetector;
    //private PathSmoother<SortedIntList.Node, Vector2> pathSmoother;

    public GDXAIPathfinder(int worldWidth, int worldHeight) {
        graph = new GDXAIGraph(worldWidth, worldHeight);
        collisionDetector = new CollisionDetector(graph);
        try {
            indexedAStarPathFinder = new IndexedAStarPathFinder(graph);
            //pathSmoother = new PathSmoother<SortedIntList.Node, Vector2>(new CollisionDetector(graph));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Vector2f getPath(Vector2f location) {
        location.set(Math.round(location.x), Math.round(location.y));
        if (goal == null) {
            return new Vector2f(0, 0);
        }
//        location.set(location.x * MyGame.WIDTH_RATIO, location.y * MyGame.HEIGHT_RATIO);
//        Vector2f goalPos = new Vector2f(goal.getPosition().x * MyGame.WIDTH_RATIO,
//                goal.getPosition().y * MyGame.HEIGHT_RATIO);
        Vector2f goalPos = new Vector2f(Math.round(goal.getPosition().x), Math.round(goal.getPosition().y));
        Vector2f returnVector;
        Ray<Vector2> ray = new Ray<Vector2>(Vector2fToVector2(location), Vector2fToVector2(goalPos));

        if (!collisionDetector.collides(ray)) {
            returnVector = new Vector2f(goalPos.x - location.x, goalPos.y - location.y);
//            returnVector.normalize();
//            return returnVector;
        } else {
            System.out.println("Can't see player");
            Heuristic<SortedIntList.Node> heuristic;
            GraphPath<SortedIntList.Node> outPath;

            heuristic = new GDXAIHeuristic(graph);
            outPath = new DefaultGraphPath<SortedIntList.Node>();

            try {
                indexedAStarPathFinder.searchNodePath(graph.getNode(location),
                        graph.getNode(goalPos), heuristic, outPath);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (outPath.getCount() < 2) {
                returnVector = new Vector2f(0, 0);
            } else {
                Vector2f current = graph.getPosition(outPath.get(0));
                Vector2f next = graph.getPosition(outPath.get(1));

                returnVector = new Vector2f(next.x - current.x, next.y - current.y);
            }
        }
        // Translating from physics to graphics pixels
//        returnVector.set(returnVector.x / MyGame.WIDTH_RATIO, returnVector.y / MyGame.HEIGHT_RATIO);
//        returnVector.normalize();

        return returnVector;
    }

    private Vector2 Vector2fToVector2(Vector2f vector2f) {
        return new Vector2(vector2f.x, vector2f.y);
    }

    @Override
    public void setObstacle(Vector2f obstacle) {
        int x = (int) obstacle.x;
        int y = (int) obstacle.y;

        // Assumes objects are not much larger than a grid 'tile'

        boolean alignedX = Math.abs(obstacle.x - x) < 0.2f;
        boolean alignedY = Math.abs(obstacle.y - y) < 0.2f;

        graph.setObstacle(x, y);

        if (!alignedX) {
            graph.setObstacle(x + 1, y);
            if (!alignedY) {
                graph.setObstacle(x + 1, y + 1);
            }
        }
        if (!alignedY) {
            graph.setObstacle(x, y + 1);
        }
    }

    public void setObstacles(List<Vector2f> obstacles) {
        for (Vector2f obstacle : obstacles) {
            setObstacle(obstacle);
        }
    }

    @Override
    public void setGoal(ModelActor goal) {
        this.goal = goal;
    }
}
