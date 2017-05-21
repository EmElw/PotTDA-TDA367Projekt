package com.pottda.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.*;
import com.pottda.game.model.XMLEnemy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magnus on 2017-05-21.
 */
public class MyXMLReader {


    public XMLEnemy parseEnemy(FileHandle file) {
        try {
            XmlReader xml = new XmlReader();

            List<XMLEnemy> list = new ArrayList<XMLEnemy>();

            Element root = xml.parse(file);

            if (root.getName().equals("enemy")) {
                return new XMLEnemy(root.getChildByName("name").getText(),
                        root.getChildByName("scorevalue").getText(),
                        root.getChildByName("difficulty").getText(),
                        root.getChildByName("behaviour").getText(),
                        root.getChildByName("inventoryname").getText(),
                        root.getChildByName("spriteenum").getText());
            } else {
                throw new IOException("trying to parse non-enemy");
            }
        } catch (IOException e) {
            throw new Error("failure in trying to parse fileHandle: " + file.toString() + ":", e);
        }
    }

}
