package com.pottda.game.pathfindingGDXAI;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.Graph;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SortedIntList;

import javax.vecmath.Vector2f;

public class GDXAIGraph implements IndexedGraph<SortedIntList.Node>, Graph<SortedIntList.Node> {
    public final static float OBSTACLE_COST = 1000f;
    public final static float EMPTY_COST = 1f;
    public final static int MAX_CONNECTIONS_PER_NODE = 4;

    protected Array<GDXAIConnection> connections;
    protected Array<SortedIntList.Node> nodes;
    protected final int width;
    protected final int height;

    public GDXAIGraph(int width, int height) {
        this.width = width;
        this.height = height;

        nodes = new Array<SortedIntList.Node>(width * height);
        nodes.ordered = true;
        for (int i = 0, n = width * height; i < n; i++) {
            nodes.add(new SortedIntList.Node());
        }

        connections = new Array<GDXAIConnection>();
        connections.ordered = true;
        // Set up connections between neighbouring nodes
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Connections left and right
                for (int i = Math.max(0, x - 1); i < Math.min(width, x + 1); i++) {
                    if (i != x) {
                        connections.add(new GDXAIConnection());
                        connections.get(connections.size - 1).fromNode = nodes.get(x * width + y);
                        connections.get(connections.size - 1).toNode = nodes.get(i * width + y);
                        connections.get(connections.size - 1).cost = EMPTY_COST;
                    }
                }
                // Connections up and down
                for (int i = Math.max(0, y - 1); i < Math.min(height, y + 1); i++) {
                    if (i != y) {
                        connections.add(new GDXAIConnection());
                        connections.get(connections.size - 1).fromNode = nodes.get(x * width + y);
                        connections.get(connections.size - 1).toNode = nodes.get(x * width + i);
                        connections.get(connections.size - 1).cost = EMPTY_COST;
                    }
                }
            }
        }
    }

//    public Array<GDXAIConnection> getConnections(Object fromNode) {
//        Array<GDXAIConnection> returnArray = new Array<GDXAIConnection>();
//
//        for (int i = 0, n = connections.size; i < n && returnArray.size < MAX_CONNECTIONS_PER_NODE; i++) {
//            if (connections.get(i).fromNode.equals(fromNode)) {
//                returnArray.add(connections.get(i));
//            }
//        }
//
//        return returnArray;
//    }

    public SortedIntList.Node getNode(Vector2f position) {
        return nodes.get(Math.round(position.x) * width + Math.round(position.y));
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

    /**
     * Checks if the node at x y is an obstacle
     * @param x
     * @param y
     * @return True if obstacle, otherwise false
     */
    public Boolean isObstacle(int x, int y) {
        SortedIntList.Node node = getNode(new Vector2f(x, y));

        for (int i = 0, n = connections.size; i < n; i++) {
            if (connections.get(i).toNode.equals(node)) {
                return Math.abs(connections.get(i).cost - OBSTACLE_COST) < 0.01f;
            }
        }
        return false;
    }

    @Override
    public int getIndex(SortedIntList.Node node) {
        return nodes.indexOf(node, false);
    }

    @Override
    public int getNodeCount() {
        return nodes.size;
    }

    @Override
    public Array<Connection<SortedIntList.Node>> getConnections(SortedIntList.Node fromNode) {
        Array<Connection<SortedIntList.Node>> returnArray =
                new Array<Connection<SortedIntList.Node>>();

        for (int i = 0, n = connections.size; i < n && returnArray.size < MAX_CONNECTIONS_PER_NODE; i++) {
            if (connections.get(i).fromNode.equals(fromNode)) {
                returnArray.add(connections.get(i));
            }
        }

        return returnArray;
    }
}
