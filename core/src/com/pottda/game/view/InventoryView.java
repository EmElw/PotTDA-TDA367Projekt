package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;

/**
 * Created by Mr Cornholio on 15/05/2017.
 */
public class InventoryView {
    private Stage stage;
    private Table table;

    public InventoryView(Stage inventoryStage) {
        this.stage = inventoryStage;
        create();
    }

    public void create() {
        Gdx.input.setInputProcessor(stage);

        Skin mySkin = new Skin(Gdx.files.internal("skin/quantum-horizon-ui.json"));

        // See Scene2D.ui github page for instructions to design the page
        Label storageLabel = new Label("Storage", mySkin);
        Label inventoryLabel = new Label("Inventory", mySkin);

        // Create storage
        VerticalGroup storageTable = new VerticalGroup();
        ScrollPane storageScroll = new ScrollPane(storageTable);
        Container storage = new Container(storageScroll);

        // Create inventory
        Table inventoryTable = new Table();
        Container inventory = new Container(inventoryTable);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.setDebug(true);

        table.add(storageLabel);
        table.add(inventoryLabel);
        table.row();
        table.add(storage).fill();
        table.add(inventory).expand().fill();
     }

    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void renderInventory () {
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}
