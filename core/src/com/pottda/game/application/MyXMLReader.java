package com.pottda.game.application;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.pottda.game.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyXMLReader {

    private final XmlReader xml = new XmlReader();

    void generateXMLAssets() {
        generateInventories(this);
        generateEnemies(this);
        generateEnemyGroups(this);
    }

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


            if (root.getName().equals("enemygroup")) {
                List<String> list = new ArrayList<String>();
                int difficulty = root.getInt("difficulty");

                String name = root.getAttribute("name");
                for (Element e : root.getChildrenByName("enemy")) {
                    list.add(e.getText());
                }
                return new XMLEnemyGroup(name, list, difficulty);
            } else {
                throw new IOException("no enemygroup in root");
            }
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
                for (Element e : root.getChildrenByName("sizedItem")){
                    items.add(parseSizedItem(e));
                }
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

    private XMLItem parseSizedItem(Element e) throws IOException {
        if (e.getName().equals("sizedItem")) {
            return new XMLSizedItem(
                    e.getAttribute("name"),
                    e.getIntAttribute("x"),
                    e.getIntAttribute("y"),
                    e.getIntAttribute("orientation"),
                    e.getIntAttribute("size"));
        }
        throw new IOException("no sizedItem in root");
    }

    private void generateInventories(MyXMLReader reader) {

        FileHandle folder = Gdx.files.internal("inventoryblueprint");

        List<FileHandle> contents = Arrays.asList(folder.list("xml"));
        try {
            for (FileHandle f : contents) {
                InventoryBlueprint.newBlueprint(reader.parseInventory(f));
            }
        } catch (Exception e) {
            throw new Error("failed to generate inventory blueprints: ", e);
        }
    }

    private void generateEnemies(MyXMLReader reader) {
        FileHandle folder = Gdx.files.internal("enemies");

        List<FileHandle> contents = Arrays.asList(folder.list("xml"));
        try {
            for (FileHandle f : contents) {
                EnemyBlueprint.newBlueprint(reader.parseEnemy(f));
            }
        } catch (Exception e) {
            throw new Error("failed to generate enemy blueprints: ", e);
        }
    }

    private void generateEnemyGroups(MyXMLReader reader) {
        FileHandle folder = Gdx.files.internal("enemygroups");

        List<FileHandle> contents = Arrays.asList(folder.list("xml"));
        try {
            for (FileHandle f : contents) {
                EnemyGroup.newGroup(reader.parseEnemyGroup(f));
            }
        } catch (Exception e) {
            throw new Error("failed to generate enemy blueprints: ", e);
        }
    }
}
