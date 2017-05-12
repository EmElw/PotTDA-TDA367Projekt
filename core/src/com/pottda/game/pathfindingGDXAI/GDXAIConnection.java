package com.pottda.game.pathfindingGDXAI;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.SortedIntList;

public class GDXAIConnection implements Connection {
    public float cost = 1f;
    public SortedIntList.Node fromNode;
    public SortedIntList.Node toNode;

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public Object getFromNode() {
        return fromNode;
    }

    @Override
    public Object getToNode() {
        return toNode;
    }
}
