package com.pottda.game.pathfindingGDXAI;

import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.utils.SortedIntList;

public class EuclideanDistanceHeuristic implements Heuristic<SortedIntList.Node> {
    private final static float NORMAL_COST = 1f;
    private int width;
    private int height;
    private GDXAIGraph graph;

    public EuclideanDistanceHeuristic(GDXAIGraph graph) {
        this.graph = graph;
        width = graph.width;
        height = graph.height;
    }

    @Override
    public float estimate(SortedIntList.Node node, SortedIntList.Node endNode) {
        int startX = graph.nodes.indexOf(node, false) / width;
        int startY = graph.nodes.indexOf(node, false) % width;

        int goalX = graph.nodes.indexOf(endNode, false) / width;
        int goalY = graph.nodes.indexOf(endNode, false) % width;

        int dx = Math.abs(startX - goalX);
        int dy = Math.abs(startY - goalY);

        return NORMAL_COST * (float) Math.sqrt(dx * dx + dy * dy);
    }
}
