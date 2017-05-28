package com.pottda.game.application;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pottda.game.controller.ControllerOptions;
import com.pottda.game.model.Constants;
import com.pottda.game.model.InventoryBlueprint;
import com.pottda.game.model.Item;
import com.pottda.game.model.Storage;
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

        switch (Gdx.app.getType()) {
            case Android:
                ControllerOptions.controllerSettings = ControllerOptions.ControllerMode.TOUCH_JOYSTICK;
                break;
            case Desktop:
                ControllerOptions.controllerSettings = ControllerOptions.ControllerMode.KEYBOARD_MOUSE;
                break;
        }

        /*Storage storage = new Storage();
        storage.addItem(new SimpleCannon());
        storage.addItem(new SimpleCannon());
        storage.addItem(new MultiShot());
        storage.addItem(new Switcher());
        storage.addItem(new BouncingBallCannon());
        storage.addItem(new ChainAttack());

        setScreen(new InventoryManagementScreen(this, null,
                InventoryBlueprint.getInventory("sizedItemTestInv.xml"), storage));*/
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
        itemList.add(new PenetratingCannon());
        itemList.add(new MultiShot());
        itemList.add(new SimpleCannon());
        itemList.add(new Switcher());

        for (ItemSize size : ItemSize.values()) {
            DamageItem damageItem = new DamageItem();
            damageItem.setSize(size);
            itemList.add(damageItem);

            HealthItem healthItem = new HealthItem();
            healthItem.setSize(size);
            itemList.add(healthItem);

            ProjectileSpeedItem projectileSpeedItem = new ProjectileSpeedItem();
            projectileSpeedItem.setSize(size);
            itemList.add(projectileSpeedItem);

            SpeedItem speedItem = new SpeedItem();
            speedItem.setSize(size);
            itemList.add(speedItem);
        }

        AtlasCreator.createAtlas(itemList);
    }
}
