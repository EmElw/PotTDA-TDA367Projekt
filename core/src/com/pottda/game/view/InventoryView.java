package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Scaling;
import com.pottda.game.model.Inventory;
import com.pottda.game.model.Storage;

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

        // Create table to hold storage section
        storageTable = new Table();

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.setDebug(true);

        // Create storage & scrollpane for storage
        ScrollPane scroll = new ScrollPane(storageTable);
        scroll.layout();
        scroll.setForceScroll(false, true);
        scroll.setOverscroll(false, true);
        Table storage = new Table();
        storage.add(scroll).height(stage.getHeight()-25);

        // Add labels and storage/inventory table
        table.add(storageLabel);
        table.add(inventoryLabel);
        table.row();
        table.add(storage).fill();
     }

    public void parseStorage(Storage storageMap) {
        if(storageMap.isUpdate()) {
            storageTable.clearChildren();
            storageMap.setUpdate(false);
            createStorageView(storageMap);
        }

    }

    public void parseInventory(Inventory inventory) {
        createInventoryView(inventory);
    }

    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void render() {
        stage.act();
        stage.draw();
    }

    private void createStorageView(Storage storageMap) {
        for (String s: storageMap.getMap().keySet()) {
            addToStorageView(s,storageMap.getNrOf(s));
        }
    }

    private void createInventoryView(Inventory inventory) {
        // Create inventory
    }

    public void dispose() {
        stage.dispose();
    }

    /**
     * Takes an item and shows it on the storage display
     *
     * //@param item item to show up in storage
     */
    public void addToStorageView(String itemName, int itemCount) {
        // Create a table to hold name + image
        Button itemGroupTable = new Button(mySkin);
        Table internalItemGroupTable = new Table();
        itemImage = new Image(atlas.findRegion(itemName));

        // Label hold items name
        Label itemNameLabel = new Label(itemName, mySkin);
        itemNameLabel.setFontScale(0.75f, 0.75f);
        internalItemGroupTable.add(itemNameLabel).left();
        internalItemGroupTable.row();

        // Label holds item count
        Label itemCountLabel = new Label("#"+Integer.toString(itemCount), mySkin);
        itemCountLabel.setFontScale(0.5f, 0.5f);
        internalItemGroupTable.add(itemCountLabel).right();

        Image itemImage = this.itemImage;

        // Add a label and image to the table and fit the image
        itemGroupTable.add(internalItemGroupTable).left().spaceRight(10);
        itemGroupTable.add(itemImage).width(100).height(100);
        itemGroupTable.row();
        itemImage.setScaling(Scaling.fit);
        itemGroupTable.setDebug(false);

        // Add the table to our main storage table
        storageTable.add(itemGroupTable).fill();
        storageTable.row();
    }

    /**
     * Converts an image to a texture to be used
     * @param item item containing the image you want converted
     */
}
