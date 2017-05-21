package com.pottda.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.*;
import com.pottda.game.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magnus on 2017-05-21.
 */
public class MyXMLReader {

    private XmlReader xml = new XmlReader();

    /**
     * Parses a .xml-file containing data for an enemy
     *
     * @param file a {@link FileHandle}
     * @return a {@link XMLEnemy}
     */
    public XMLEnemy parseEnemy(FileHandle file) {
        try {
            Element root = xml.parse(file);
            if (root.getName().equals("enemy")) {
                return new XMLEnemy(root.getChildByName("name").getText(),
                        root.get("scorevalue"),
                        root.get("difficulty"),
                        root.get("behaviour"),
                        root.get("inventoryname"),
                        root.get("spriteenum"));
            } else {
                throw new IOException("no enemy in root");
            }
        } catch (IOException e) {
            throw new Error("failure in trying to parse fileHandle: " + file.toString() + ":", e);
        }
    }

    /**
     * Parses a .xml-file containing data for an enemygroup
     *
     * @param file a {@link FileHandle}
     * @return a {@link XMLEnemyGroup}
     */
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

    /**
     * Parses a .xml-file containing data for an inventory
     *
     * @param file a {@link FileHandle}
     * @return a {@link XMLInventory}
     */
    public XMLInventory parseInventory(FileHandle file) {
        try {
            Element root = xml.parse(file);
            if (root.getName().equals("inventory")) {
                List<XMLItem> items = new ArrayList<XMLItem>();
                for (Element e : root.getChildrenByName("item")) {
                    items.add(parseItem(e));
                }
                return new XMLInventory(
                        file.name(),
                        items,
                        root.getIntAttribute("w"),
                        root.getIntAttribute("h"));

            } else {
                throw new IOException("no inventory in root");
            }
        } catch (IOException e) {
            throw new Error("failure in trying to parse fileHandle: " + file.toString() + ":", e);
        }
    }

    /**
     * Parses an {@link Element} containing an item
     *
     * @param e a {@link Element}
     * @return an {@link XMLItem}
     * @throws IOException if the root element in e is not equal to "item"
     */
    private XMLItem parseItem(Element e) throws IOException {
        if (e.getName().equals("item")) {
            return new XMLItem(
                    e.getAttribute("name"),
                    e.getIntAttribute("x"),
                    e.getIntAttribute("y"),
                    e.getIntAttribute("orientation"));
        }
        throw new IOException("no item in root");
    }

}