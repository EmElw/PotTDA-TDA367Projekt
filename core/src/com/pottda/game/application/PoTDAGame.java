package com.pottda.game.application;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pottda.game.model.*;
import com.pottda.game.model.Constants;
import com.pottda.game.model.items.*;
import com.pottda.game.view.AtlasCreator;

import java.util.LinkedList;

public class PoTDAGame extends Game {

    private SpriteBatch batch;

    @Override
    public void create() {
        Gdx.graphics.setTitle(Constants.GAME_TITLE);

        createAtlas();

        batch = new SpriteBatch();

        MyXMLReader reader = new MyXMLReader();
        reader.generateXMLAssets();

        setScreen(new MenuScreen(this));

//        Storage storage = new Storage();
//        storage.addItem(new SimpleCannon());
//        storage.addItem(new SimpleCannon());
//        storage.addItem(new MultiShot());
//        storage.addItem(new Switcher());
//        storage.addItem(new BouncingBallCannon());
//        storage.addItem(new ChainAttack());
//
//        setScreen(new InventoryManagementScreen(this, null, InventoryBlueprint.getInventory("testInv2.xml"), storage));
    }

    @Override
    public void render() {
        if (screen != null) {
            ((AbstractScreen) screen).render(batch, Gdx.graphics.getDeltaTime());
        }
    }

    private void createAtlas() {
        LinkedList<Item> itemList = new LinkedList<Item>();
        itemList.add(new BouncingBallCannon());
        itemList.add(new ChainAttack());
        itemList.add(new MultiShot());
        itemList.add(new SimpleCannon());
        itemList.add(new Switcher());

        AtlasCreator.createAtlas(itemList);
    }
}
