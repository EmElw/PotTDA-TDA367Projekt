package com.pottda.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.*;
import com.pottda.game.model.EnemyBlueprint;
import com.pottda.game.model.XMLEnemy;
import com.pottda.game.model.XMLEnemyGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magnus on 2017-05-21.
 */
public class MyXMLReader {

    XmlReader xml = new XmlReader();

    public XMLEnemy parseEnemy(FileHandle file) {
        try {
            Element root = xml.parse(file);
            return enemyFromElement(root);
        } catch (IOException e) {
            throw new Error("failure in trying to parse fileHandle: " + file.toString() + ":", e);
        }
    }

    public XMLEnemyGroup parseEnemyGroup(FileHandle file) {
        try {

            Element root = xml.parse(file);

            List<String> list = new ArrayList<String>();
            int difficulty;

            if (root.getName().equals("enemygroup")) {
                difficulty = root.getInt("difficulty");

                for (Element e : root.getChildrenByName("enemy")) {
                    list.add(e.getText());
                }
            } else {
                throw new IOException("no enemygroup in root");
            }
            return new XMLEnemyGroup(list, difficulty);
        } catch (IOException e) {
            throw new Error("failure in trying to parse fileHandle: " + file.toString() + ":", e);
        }
    }

    private XMLEnemy enemyFromElement(Element e) throws IOException {
        if (e.getName().equals("enemy")) {
            return new XMLEnemy(e.getChildByName("name").getText(),
                    e.get("scorevalue"),
                    e.get("difficulty"),
                    e.get("behaviour"),
                    e.get("inventoryname"),
                    e.get("spriteenum"));
        } else {
            throw new IOException("no enemy in root");
        }
    }

}
