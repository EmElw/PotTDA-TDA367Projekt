package com.pottda.game.pathfindingGDXAI;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.Graph;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SortedIntList;

public class GDXAIGraph implements Graph {
    public final static float OBSTACLE_COST = 1000f;
    public final static int MAX_CONNECTIONS_PER_NODE = 4;

    private Array<GDXAIConnection> connections;
    private Array<SortedIntList.Node> nodes;
    private final int width;
    private final int height;

    public GDXAIGraph(int width, int height) {
        this.width = width;
        this.height = height;

        nodes = new Array<SortedIntList.Node>(width * height);
        for (int i = 0, n = width * height; i < n; i++) {
            nodes.add(new SortedIntList.Node());
        }

        connections = new Array<GDXAIConnection>();
        // Set up connections between neighbouring nodes
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Connections left and right
                for (int i = Math.max(0, x - 1); i < Math.min(width, x + 1); i++) {
                    if (i != x) {
                        connections.add(new GDXAIConnection());
                        connections.get(connections.size - 1).fromNode = nodes.get(x * width + y);
                        connections.get(connections.size - 1).toNode = nodes.get(i * width + y);
                    }
                }
                // Connections up and down
                for (int i = Math.max(0, y - 1); i < Math.min(height, y + 1); i++) {
                    if (i != y) {
                        connections.add(new GDXAIConnection());
                        connections.get(connections.size - 1).fromNode = nodes.get(x * width + y);
                        connections.get(connections.size - 1).toNode = nodes.get(x * width + i);
                    }
                }
            }
        }
    }

    @Override
    public Array<GDXAIConnection> getConnections(Object fromNode) {
        Array<GDXAIConnection> returnArray = new Array<GDXAIConnection>();

        for (int i = 0, n = connections.size; i < n && returnArray.size < MAX_CONNECTIONS_PER_NODE; i++) {
            if (connections.get(i).fromNode.equals(fromNode)) {
                returnArray.add(connections.get(i));
            }
        }

        return returnArray;
    }

    public void setObstacle(int x, int y) {
        SortedIntList.Node obstacleNode = nodes.get(x * width + y);
        int changedConntections = 0;
        for (int i = 0, n = connections.size; i < n && changedConntections < MAX_CONNECTIONS_PER_NODE; i++) {
            if (connections.get(i).toNode.equals(obstacleNode)) {
                connections.get(i).cost = OBSTACLE_COST;
                changedConntections++;
            }
        }
    }
}
