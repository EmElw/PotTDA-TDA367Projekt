package com.pottda.game.model;

import com.badlogic.gdx.files.FileHandle;
import com.pottda.game.application.MyXMLReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Magnus on 2017-05-22.
 */
public class WaveManagerTest {

    @BeforeClass
    public static void setUp() {
        String basePath = new File("").getAbsolutePath();
        String enemyPath = basePath.
                replace("\\core", "").  // No one must know of this blasphemy
                concat("\\android\\assets\\enemies");
        String groupPath = basePath.
                replace("\\core", "").  // No one must know of this blasphemy
                concat("\\android\\assets\\enemygroups");

        generateEnemies(new FileHandle(enemyPath), new MyXMLReader());
        generateGroups(new FileHandle(groupPath), new MyXMLReader());
    }

    private WaveManager waveManager;

    @Before
    public void instantiating() {
        waveManager = new WaveManager();
    }

    @Test
    public void test() {
        waveManager.progressTime((long) 10E5);

        Assert.assertFalse(waveManager.getToSpawn().isEmpty());
    }

    private static void generateEnemies(FileHandle folder, MyXMLReader reader) {

        List<FileHandle> contents = Arrays.asList(folder.list("xml"));
        try {
            for (FileHandle f : contents) {
                EnemyBlueprint.newBlueprint(reader.parseEnemy(f));
            }
        } catch (Exception e) {
            throw new Error("failed to generate enemy blueprints: ", e);
        }
    }


    private static void generateGroups(FileHandle folder, MyXMLReader reader) {

        List<FileHandle> contents = Arrays.asList(folder.list("xml"));
        try {
            for (FileHandle f : contents) {
                EnemyGroup.newGroup(reader.parseEnemyGroup(f));
            }
        } catch (Exception e) {
            throw new Error("failed to generate group: ", e);
        }
    }

}
