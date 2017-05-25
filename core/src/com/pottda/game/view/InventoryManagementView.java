package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
    private Table storageTable;
    private Table inventoryTable;
    private Skin mySkin = new Skin(Gdx.files.internal("skin/quantum-horizon-ui.json"));

    private Table table;
    private Image itemImage;

    private WorkingImageTable workingItemTable;

    private List<InventoryManagementListener> listeners;

    public InventoryManagementView(final Stage stage) {
        this.stage = stage;
        stage.setDebugAll(true);
        this.stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent evt, float x, float y, int index, int button) {
                try {
                    if (evt.getTarget() instanceof StorageImage) {
                        if (workingItemTable != null)
                            workingItemTable.clearChildren();
                        workingItemTable = new WorkingImageTable(((StorageImage) evt.getTarget()).itemName);
                    } else if (evt.getTarget() instanceof ItemImage) {
                        for (InventoryManagementListener iml : listeners) {
                            iml.inventoryItemTouched(((ItemImage) evt.getTarget()).item);
                        }
                        return true;
                    }
                } catch (ClassCastException e) {
                    return false;
                }
                return false;
            }

            @Override
            public void touchDragged(InputEvent evt, float x, float y, int index) {

            }

            @Override
            public void touchUp(InputEvent evt, float x, float y, int index, int button) {
                if (evt.getTarget() instanceof StorageImage) {
                    StorageImage actor = (StorageImage) evt.getTarget();
                } else if (evt.getTarget() instanceof ItemImage) {
                    ItemImage actor = (ItemImage) evt.getTarget();
                }
            }

            /*@Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                workingItemGroup.setPosition(x, y);
                return true;
            }*/
        });
        this.listeners = new ArrayList<InventoryManagementListener>();
        create();
    }

    public void create() {
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);


        // Create table to hold storage section
        storageTable = new Table();
        // Create storage & scrollpane for storage
        ScrollPane scroll = new ScrollPane(storageTable);
        scroll.layout();
        scroll.setForceScroll(false, true);
        scroll.setOverscroll(false, false);
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

    public void dispose() {
        stage.dispose();
    }

    // Interesting stuff

    /**
     * Transforms a coordinate in the top-level stage into a discreet coordinate inside the inventory
     * <p>
     * (i.e. the same as those used internally by items)
     *
     * @param x
     * @param y
     * @return a {@link Point2i}
     */
    private Point2i toInventoryCoordinate(float x, float y) {

        // Convert to coordinate relative to the inventory group

        // Convert to coordinate in inventory

        return null;
    }

    public void updateStorageTable(Storage storage) {
        for (String s : storage.getItems()) {
            addToStorageTable(s, storage.getNrOf(s));
        }
    }

    private void addToStorageTable(final String itemName, int itemCount) {
        // Create a table to hold name + image
        Table storageTable = new Table();
        final StorageImage storageImage = new StorageImage(itemName);
        Stack storageStack = new Stack();
        //storageImage.setColor(0.03f, 0.69f, 0.73f, 1);  // TODO test only

//        storageImage.addListener(
//                new InputListener() {
//
//                    @Override
//                    public boolean touchDown(InputEvent evt, float x, float y, int pointer, int button) {
//                        for (InventoryManagementListener iml : listeners) {
//                            iml.storageItemTouched(itemName);
//                        }
//                        return true;
//                    }
//
//                    @Override
//                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//
//                    }
//                });

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
        storageTable.add(internalItemGroupTable).left().spaceRight(10);
        storageTable.add(itemImage);
        storageTable.setTouchable(Touchable.disabled);
        itemImage.setScaling(Scaling.fit);
        storageStack.add(storageImage);
        storageStack.add(storageTable);

        // Add the table to our main storage table
        this.storageTable.add(storageStack).fill();
        this.storageTable.row();
    }

    public void updateInventoryGroup(Inventory inventory) {
        // Create inventory
        WidgetGroup inventoryGroup = new WidgetGroup();

        List<Point2i> connections = new ArrayList<Point2i>();

        for (Item i : inventory.getItems()) {
            TextureAtlas.AtlasRegion region = atlas.findRegion(i.getName());
            region.flip(false, true);
            ItemImage itemImage = new ItemImage(region, i);

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

//            itemImage.addListener(new InputListener() {
//                Item item;
//
//                @Override
//                public boolean touchDown(InputEvent evt, float x, float y, int pointer, int button) {
//                    for (InventoryManagementListener iml : listeners) {
//                        iml.inventoryItemTouched(item);
//                    }
//                    return true;
//                }
//            });
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
        inventoryTable.add(inventoryGroup).expand().bottom().left().pad(2 * SIZE);
    }

    // Boring stuff

    public void addListener(InventoryManagementListener listener) {
        listeners.add(listener);
    }

    public void removeListener(InventoryManagementListener listener) {
        listeners.remove(listener);
    }

    // Inner classes

    private class StorageImage extends Image {

        private String itemName;    // The external item name associated with this button

        private StorageImage(String itemName) {
            super(new Texture(Gdx.files.internal("menu/storageButtonBackground.png")));
            this.itemName = itemName;
        }
    }

    private class ItemImage extends Image {
        private Item item;

        private ItemImage(TextureAtlas.AtlasRegion region, Item item) {
            super(region);
            this.item = item;
        }
    }

    private class WorkingImageTable extends Table {

        private final Drawable rotateRightButtonDrawable = new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("rotateRightButton.png"))));
        private final Drawable rotateLeftButtonDrawable = new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("rotateLeftButton.png"))));
        private final Drawable acceptButtonDrawable = new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("acceptButton.png"))));
        private final Drawable discardButtonDrawable = new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("discardButton.png"))));


        private ImageButton rotateRightButton;
        private ImageButton rotateLeftButton;
        private ImageButton acceptButton;
        private ImageButton discardButton;


        private WorkingImageTable(String itemName) {
            rotateRightButton = new ImageButton(rotateRightButtonDrawable);
            rotateLeftButton = new ImageButton(rotateLeftButtonDrawable);
            acceptButton = new ImageButton(acceptButtonDrawable);
            discardButton = new ImageButton(discardButtonDrawable);

            final Image itemImage = new Image(atlas.findRegion(itemName));
            itemImage.setOrigin(itemImage.getWidth() / 2, itemImage.getHeight() / 2);

            this.add(discardButton).left().size(25);
            this.add(acceptButton).right().size(25);
            this.row();
            this.add(itemImage).center().colspan(2)
                    .width(itemImage.getWidth()).height(itemImage.getHeight()).pad(10);
            this.row();
            this.add(rotateLeftButton).left().size(25);
            this.add(rotateRightButton).right().size(25);
            this.setPosition(500, 200);
            stage.addActor(this);

            addListener(new InputListener() {
                // Used to keep 0,0 relative distance to touch coordinate constant
                float xOffset;
                float yOffset;

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    xOffset = x;
                    yOffset = y;
                    return true;
                }

                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    setPosition(
                            ((int)(getX() + x - xOffset)/25)*25,
                            ((int)(getY() + y - yOffset)/25)*25);
                    // TODO validate if the new coordinates are legal inside the inventory (send event to the listener if they are at all within inventory?)
                }
            });

            rotateLeftButton.addListener(new InputListener() {


                @Override
                public boolean touchDown(InputEvent evt, float x, float y, int index, int button) {
                    itemImage.rotateBy(90);
                    return true;
                }
            });
            rotateRightButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent evt, float x, float y, int index, int button) {
                    itemImage.rotateBy(-90);
                    return true;
                }
            });

            discardButton.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent evt, float x, float y, int index, int button) {
                    clearChildren();
                    return true;
                }
            });
        }
    }
}
