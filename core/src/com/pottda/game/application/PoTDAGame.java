package com.pottda.game.application;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pottda.game.model.*;
import com.pottda.game.model.Constants;
import com.pottda.game.model.builders.AbstractModelBuilder;
import com.pottda.game.model.items.*;
import com.pottda.game.view.AtlasCreator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.pottda.game.application.GameState.INVENTORY;
import static com.pottda.game.application.GameState.MAIN_CHOOSE;
import static com.pottda.game.application.GameState.MAIN_CONTROLS;
import static com.pottda.game.application.GameState.MAIN_MENU;
import static com.pottda.game.application.GameState.OPTIONS;
import static com.pottda.game.application.GameState.PAUSED;
import static com.pottda.game.application.GameState.RESTARTING;
import static com.pottda.game.application.GameState.gameState;
import static com.pottda.game.model.Constants.HEIGHT_VIEWPORT;

public class PoTDAGame extends Game {

    private SpriteBatch batch;

    @Override
    public void create() {
        Gdx.graphics.setTitle(Constants.GAME_TITLE);

        createAtlas();

        batch = new SpriteBatch();

        MyXMLReader reader = new MyXMLReader();
        reader.generateXMLAssets();

        Storage storage = new Storage();
        storage.addItem(new SimpleCannon());
        storage.addItem(new SimpleCannon());
        storage.addItem(new MultiShot());
        storage.addItem(new Switcher());
        storage.addItem(new BouncingBallCannon());
        storage.addItem(new ChainAttack());

        setScreen(new InventoryManagementScreen(this, InventoryBlueprint.getInventory("testInv2.xml"), storage));
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
