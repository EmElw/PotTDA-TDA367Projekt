package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.pottda.game.model.Inventory;
import com.pottda.game.model.Item;
import com.pottda.game.model.Storage;

import javax.vecmath.Point2i;

import java.util.*;
import java.util.List;

import static com.pottda.game.view.AtlasCreator.SIZE;
import static com.pottda.game.view.AtlasCreator.atlas;

/**
 * Created by Mr Cornholio on 15/05/2017.
 */
public class InventoryManagementView {
    private Stage stage;
    private Table table;
    private Table storageTable;
    private Table inventoryTable;
    private Skin mySkin = new Skin(Gdx.files.internal("skin/quantum-horizon-ui.json"));
    private Image itemImage;

    private Image workingItemImage;

    private List<InventoryManagementListener> listeners;

    public InventoryManagementView(Stage stage) {
        this.stage = stage;
        this.stage.addListener(new InputListener() {
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                workingItemImage.setPosition(x, y);
                return true;
            }
        });
        this.listeners = new ArrayList<InventoryManagementListener>();
        create();
    }

    public void create() {
        Gdx.input.setInputProcessor(stage);

        workingItemImage = new Image();
        workingItemImage.setZIndex(1000);
        stage.addActor(workingItemImage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.setDebug(true);

        // Create table to hold storage section
        storageTable = new Table();
        // Create storage & scrollpane for storage
        ScrollPane scroll = new ScrollPane(storageTable);
        scroll.layout();
        scroll.setForceScroll(false, true);
        scroll.setOverscroll(false, true);
        Table storage = new Table();
        storage.add(scroll).height(stage.getHeight() - 25);

        // Create group to hold inventory section
        inventoryTable = new Table();

        // Create labels for storage and inventory
        Label storageLabel = new Label("Storage", mySkin);
        Label inventoryLabel = new Label("Inventory", mySkin);

        // Add labels and storage/inventory table
        table.add(storageLabel);
        table.add(inventoryLabel);
        table.row();
        table.add(storage).fill();
        table.add(inventoryTable).fill().expand();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void render() {
        stage.act();
        stage.draw();
    }

    public void updateStorageTable(Storage storage) {
        for (String s : storage.getItems()) {
            addToStorageTable(s, storage.getNrOf(s));
        }
    }

    /**
     * Takes an item and shows it on the storage display
     * <p>
     *
     * @param itemName a {@link String} with the external item name
     */
    private void addToStorageTable(final String itemName, int itemCount) {
        // Create a table to hold name + image
        final Button itemButton = new Button(mySkin);
        itemButton.setColor(0.03f, 0.69f, 0.73f, 1);  // TODO test only

        itemButton.addListener(
                new InputListener() {

                    @Override
                    public boolean touchDown(InputEvent evt, float x, float y, int pointer, int button) {
                        for (InventoryManagementListener iml : listeners) {
                            iml.storageItemTouched(itemName);
                        }
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                    }
                });

        Table internalItemGroupTable = new Table();
        itemImage = new Image(atlas.findRegion(itemName));

        // Label hold items name
        Label itemNameLabel = new Label(itemName, mySkin);
        itemNameLabel.setFontScale(0.75f, 0.75f);
        internalItemGroupTable.add(itemNameLabel).left();
        internalItemGroupTable.row();

        // Label holds item count
        Label itemCountLabel = new Label("#" + Integer.toString(itemCount), mySkin);
        itemCountLabel.setFontScale(0.5f, 0.5f);
        internalItemGroupTable.add(itemCountLabel).right();

        Image itemImage = this.itemImage;

        // Add a label and image to the table and fit the image
        itemButton.add(internalItemGroupTable).left().spaceRight(10);
        itemButton.add(itemImage).width(100).height(100);
        itemButton.row();
        itemImage.setScaling(Scaling.fit);
        itemButton.setDebug(false);

        // Add the table to our main storage table
        storageTable.add(itemButton).fill();
        storageTable.row();
    }

    public void updateInventoryGroup(Inventory inventory) {
        // Create inventory
        WidgetGroup inventoryGroup = new WidgetGroup();

        List<Point2i> connections = new ArrayList<Point2i>();

        for (Item i : inventory.getItems()) {
            TextureAtlas.AtlasRegion region = atlas.findRegion(i.getName());
            region.flip(false, true);
            Image itemImage = new Image(region);

            // TODO listener

            Point2i negativeOffset = i.getBaseBottomLeft();

            connections.addAll(i.getTransformedRotatedOutputs());

            itemImage.setOrigin(
                    (float) ((0.5 - negativeOffset.x) * SIZE),
                    (float) ((0.5 - negativeOffset.y) * SIZE));
            itemImage.setRotation(i.getOrientation() * 90);
            itemImage.setPosition(
                    (i.getX() + negativeOffset.x) * SIZE,
                    (i.getY() + negativeOffset.y) * SIZE);
            inventoryGroup.addActor(itemImage);

            itemImage.addListener(new InputListener() {
                Item item;

                @Override
                public boolean touchDown(InputEvent evt, float x, float y, int pointer, int button) {
                    for (InventoryManagementListener iml : listeners) {
                        iml.inventoryItemTouched(item);
                    }
                    return true;
                }
            });
        }

        // Add images for connections
        // TODO test only use proper resource handling
        Texture connection = new Texture(Gdx.files.internal("testconnection.png"));
        Texture notConnection = new Texture(Gdx.files.internal("outputTest.png"));
        for (Point2i p : connections) {
            Image connectionImage = new Image(
                    inventory.itemAt(p) == null ? notConnection : connection);

            connectionImage.setPosition(p.x * SIZE, p.y * SIZE);
            connectionImage.setTouchable(Touchable.disabled);
            inventoryGroup.addActor(connectionImage);
        }
        inventoryGroup.validate();
        inventoryTable.add(inventoryGroup);
        inventoryTable.setDebug(true);
    }

    public void dispose() {
        stage.dispose();
    }

    public void addListener(InventoryManagementListener listener) {
        listeners.add(listener);
    }

    public void removeListener(InventoryManagementListener listener) {
        listeners.remove(listener);
    }

    public void setWorkingItemDrawable(Drawable d) {
        this.workingItemImage.setDrawable(d);
    }
}
