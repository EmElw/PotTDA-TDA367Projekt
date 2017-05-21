package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Scaling;
import com.pottda.game.model.Item;
import com.pottda.game.view.AtlasCreator;

import static com.pottda.game.view.AtlasCreator.atlas;

/**
 * Created by Mr Cornholio on 15/05/2017.
 */
public class InventoryView {
    private Stage stage;
    private Table table;
    private Table storageTable;
    private Table inventoryTable;
    private Skin mySkin = new Skin(Gdx.files.internal("skin/quantum-horizon-ui.json"));
    private Image itemImage;

    public InventoryView(Stage inventoryStage) {
        this.stage = inventoryStage;
        create();
    }

    public void create() {
        Gdx.input.setInputProcessor(stage);



        // See Scene2D.ui github page for instructions to design the page
        Label storageLabel = new Label("Storage", mySkin);
        Label inventoryLabel = new Label("Inventory", mySkin);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.setDebug(true);

        // Add labels
        table.add(storageLabel);
        table.add(inventoryLabel);
        createStorage();
        createInventory();
     }

    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void renderInventory () {
        stage.act();
        /*
        if(Storage.isUpdate()){
        storageTable.clearChildren();

        }
         */
        stage.draw();
    }

    private void createStorage() {
        // Create storage & scrollpane for storage
        storageTable = new Table();
        ScrollPane scroll = new ScrollPane(storageTable);
        scroll.layout();
        scroll.setForceScroll(false, true);
        Table storage = new Table();
        storage.add(scroll).expand().fill().height(stage.getHeight()-25);

        table.row();
        table.add(storage);
    }

    private void createInventory() {
        // Create inventory
        inventoryTable = new Table();
        Container inventory = new Container(inventoryTable);

        table.add(inventory).expand().fill();
    }

    public void dispose() {
        stage.dispose();
    }

    /**
     * Takes an item and shows it on the storage display
     *
     * //@param item item to show up in storage
     */
    public void addToStorageView(Item item) {
        /*VerticalGroup itemGroupTable = new VerticalGroup();
        imageToTexture(item);

        Label itemName = new Label(item.name, mySkin);
        Image itemImage = new Image(itemImageTexture);
        itemGroupTable.addActor(itemName);
        itemGroupTable.addActor(itemImage);

        storageTable.addActor(itemGroupTable);*/

        // Create a table to hold name + image
        Table pancakeGroupTable = new Table();
        itemImage = new Image(atlas.findRegion(item.name));

        SpriteBatch batch = new SpriteBatch();
        FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, 125, 125, false);

        Label pancakeName = new Label(item.name, mySkin);
        Image pancakeImage = itemImage;

        // Add a label and image to the table and fit the image
        pancakeGroupTable.add(pancakeName);
        pancakeGroupTable.row();
        pancakeGroupTable.add(pancakeImage).width(125).height(125);
        pancakeImage.setScaling(Scaling.fit);
        pancakeGroupTable.setDebug(true);

        // Add the table to our main storage table
        storageTable.add(pancakeGroupTable);
        storageTable.row();
    }

    /**
     * Converts an image to a texture to be used
     * @param item item containing the image you want converted
     */
}
