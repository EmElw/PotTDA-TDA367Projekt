package com.pottda.game.model;

import java.util.*;

/**
 * Blueprint-type class for a group of enemy ModelActors
 */
public class EnemyGroup {

    private static final SortedMap<Integer, List<EnemyGroup>> groups =
            new TreeMap<Integer, List<EnemyGroup>>() {
            };
    public final String name;

    /**
     * Adds a new group to the library
     *
     * @param xmlGroup a {@link XMLEnemyGroup}
     */
    public static void newGroup(XMLEnemyGroup xmlGroup) {
        int difficulty = xmlGroup.difficulty;
        EnemyGroup group = new EnemyGroup(xmlGroup);

        if (groups.containsKey(difficulty)) {
            groups.get(difficulty).add(group);
        } else {
            groups.put(difficulty, new ArrayList<EnemyGroup>());
            groups.get(difficulty).add(group);
        }
    }

    /**
     * Returns true if there are one or more {@code EnemyGroup} that
     * corresponds to the given difficulty
     *
     * @param difficulty an integer
     * @return true if there are one or more {@code EnemyGroup} with the given difficulty
     */
    public static boolean contains(int difficulty) {
        return (groups.containsKey(difficulty));
    }

    /**
     * Returns ALL groups with a difficulty equal to or less than difficulty
     *
     * @param difficulty an integer
     * @return a {@code List<EnemyGroup>}}
     */
    public static List<EnemyGroup> getGroupForDifficulty(int difficulty) {
        if (groups.containsKey(difficulty)) {
            return groups.get(difficulty);
        } else {
            return null;
        }
    }

    // --------------- instance ------------------

    /**
     * A list of the enemies contained in the group represented with {@link EnemyBlueprint}
     */
    private final List<EnemyBlueprint> enemies;

    /**
     * The difficulty rating of the group
     */
    private final int difficulty;

    /**
     * Constructs an EnemyGroup using the given {@link XMLEnemyGroup}
     *
     * @param xmlEnemyGroup an {@link XMLEnemyGroup}
     */
    private EnemyGroup(XMLEnemyGroup xmlEnemyGroup) {
        this.difficulty = xmlEnemyGroup.difficulty;
        this.enemies = new ArrayList<EnemyBlueprint>();

        for (String s : xmlEnemyGroup.enemies) {
            enemies.add(EnemyBlueprint.getBlueprint(s));
        }
        name = xmlEnemyGroup.name;
    }

    /**
     * Returns a list containing all {@code EnemyGroup} with a
     * {@code difficulty} less than or equal to difficulty
     *
     * @param difficulty an integer
     * @return a {@code List<EnemyGrop>}
     */
    public static List<EnemyGroup> getAllBelowDifficulty(int difficulty) {
        List<EnemyGroup> returnList = new ArrayList<EnemyGroup>();

        for (List<EnemyGroup> g : groups.headMap(difficulty + 1).values()) {
            returnList.addAll(g);
        }

        return returnList;
    }

    /**
     * Returns the difficulty of the group
     *
     * @return an integer
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Returns a list of {@link EnemyBlueprint} contained in the group
     *
     * @return a {@code List<EnemyBlueprint>}
     */
    public List<EnemyBlueprint> getEnemies() {
        return enemies;
    }
}
