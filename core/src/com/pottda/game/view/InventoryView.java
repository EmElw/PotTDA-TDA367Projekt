package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Scaling;
import com.pottda.game.model.Item;

/**
 * Created by Mr Cornholio on 15/05/2017.
 */
public class InventoryView {
    private Stage stage;
    private Table table;
    private VerticalGroup storageTable;
    private Table inventoryTable;
    private Skin mySkin = new Skin(Gdx.files.internal("skin/quantum-horizon-ui.json"));
    private Texture itemImageTexture;

    public InventoryView(Stage inventoryStage) {
        this.stage = inventoryStage;
        create();
    }

    public void create() {
        Gdx.input.setInputProcessor(stage);



        // See Scene2D.ui github page for instructions to design the page
        Label storageLabel = new Label("Storage", mySkin);
        Label inventoryLabel = new Label("Inventory", mySkin);

        // Create storage & scroll for storage
        storageTable = new VerticalGroup();
        ScrollPane scroll = new ScrollPane(storageTable, mySkin);
        Table storage = new Table();
        storage.add(scroll).expand().fill();

        // Create inventory
        inventoryTable = new Table();
        Container inventory = new Container(inventoryTable);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.setDebug(true);

        table.add(storageLabel);
        table.add(inventoryLabel);
        table.row();
        table.add(storage);
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

    /**
     * Takes an item and shows it on the storage display
     *
     * //@param item item to show up in storage
     */
    public void addToStorageView() {
        /*VerticalGroup itemGroupTable = new VerticalGroup();
        imageToTexture(item);

        Label itemName = new Label(item.name, mySkin);
        Image itemImage = new Image(itemImageTexture);
        itemGroupTable.addActor(itemName);
        itemGroupTable.addActor(itemImage);

        storageTable.addActor(itemGroupTable);*/

        // Create a table to hold name + image
        Table pancakeGroupTable = new Table();
        itemImageTexture = new Texture(Gdx.files.internal("pancake1.jpg"));

        Label pancakeName = new Label("Pancake", mySkin);
        Image pancakeImage = new Image(itemImageTexture);

        // Add a label and image to the table and fit the image
        pancakeGroupTable.add(pancakeName);
        pancakeGroupTable.row();
        pancakeGroupTable.add(pancakeImage).width(125).height(125);
        pancakeImage.setScaling(Scaling.fit);
        pancakeGroupTable.setDebug(true);

        // Add the table to our main storage table
        storageTable.addActor(pancakeGroupTable);
    }

    /**
     * Converts an image to a texture to be used
     * @param item item containing the image you want converted
     */
    private void imageToTexture(Item item) {
        itemImageTexture = new Texture(Gdx.files.internal(item.itemImageLocation));
    }
}
