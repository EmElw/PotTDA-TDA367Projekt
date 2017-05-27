package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
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
    private WidgetGroup inventoryGroup;

    private Table table;

    private Inventory inventory;

    private WorkingImageTable workingItemTable;

    private List<InventoryManagementListener> listeners;

    private ItemImage chosenImage;

    private boolean isFromStorage;

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
                        if (chosenImage != null)
                            chosenImage.setColor(1, 1, 1, 1);
                        workingItemTable = new WorkingImageTable(((StorageImage) evt.getTarget()).getItem());
                        isFromStorage = true;

                    } else if (evt.getTarget() instanceof ItemImage) {
                        if (workingItemTable != null)
                            workingItemTable.clearChildren();
                        if (chosenImage != null)
                            chosenImage.setColor(1, 1, 1, 1);
                        chosenImage = (ItemImage) evt.getTarget();
                        workingItemTable = new WorkingImageTable(chosenImage.item);
                        chosenImage.setColor(100, 100, 100, 0.5f);
                        isFromStorage = false;

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
        if (storageTable != null)
            storageTable.clearChildren();
        for (String s : storage.getItems()) {
            try {
                addToStorageTable(s, storage.getNrOf(s), storage.getItem(s));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addToStorageTable(String itemName, int itemCount, Item item) {
        // Create a table to hold name + image
        Table storageTable = new Table();
        final StorageImage storageImage = new StorageImage(item);
        Stack storageStack = new Stack();
        Image itemImage;
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
        if (inventoryGroup != null)
            inventoryTable.clearChildren();
        inventoryGroup = new WidgetGroup();

        this.inventory = inventory;

        List<Point2i> connections = new ArrayList<Point2i>();

        for (Item i : this.inventory.getItems()) {
            TextureAtlas.AtlasRegion region = atlas.findRegion(i.getName());
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
                    this.inventory.itemAt(p) == null ? notConnection : connection);

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

        private Item item;

        private StorageImage(Item item) {
            super(new Texture(Gdx.files.internal("menu/storageButtonBackground.png")));
            this.item = item;
        }

        private Item getItem() {
            return item;
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


        private ImageButton rotateRightButton = new ImageButton(rotateRightButtonDrawable);
        private ImageButton rotateLeftButton = new ImageButton(rotateLeftButtonDrawable);
        private ImageButton acceptButton = new ImageButton(acceptButtonDrawable);
        private ImageButton discardButton = new ImageButton(discardButtonDrawable);

        private float prePosX = 0, prePosY = 0, posX, posY;
        private boolean acceptButtonStatus = false;
        private final Image itemImage;

        private Vector2 inventoryPosition;

        private WorkingImageTable(final Item item) {

            itemImage = new Image(atlas.findRegion(item.getName()));
            Point2i negativeOffset = item.getBaseBottomLeft();
            itemImage.setOrigin(
                    (float) ((0.5 - negativeOffset.x) * SIZE),
                    (float) ((0.5 - negativeOffset.y) * SIZE));

            this.add(discardButton).left().size(SIZE);
            this.add(acceptButton).right().size(SIZE);
            this.row();
            this.add(itemImage).center().colspan(2)
                    .width(itemImage.getWidth()).height(itemImage.getHeight());
            this.row();
            this.add(rotateLeftButton).left().size(SIZE);
            this.add(rotateRightButton).right().size(SIZE);

            setAcceptButtonState(200, 200, itemImage.getRotation(), item);

            // TODO fix vectors pls
            // Vector2 vector = itemImage.localToParentCoordinates(new Vector2(itemImage.getOriginX(), itemImage.getOriginY()));

            setPosition(200, 200);

            inventoryGroup.addActor(this);

            //final Vector2 finalVector = vector;
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
                    float extraY = 0f;
                    float extraX = 0f;
                    if (itemImage.getPrefHeight() / 25 % 2 != 0) {
                        extraY = 25f / 2f;
                    }
                    if (itemImage.getPrefWidth() / 25 % 2 != 0) {
                        extraX = 25f / 2f;
                    }

                    posX = ((int) (getX() + x - xOffset) / 25) * 25 + extraX;
                    posY = ((int) (getY() + y - yOffset) / 25) * 25 + extraY;


                    setPosition(posX, posY);
                    // TODO validate if the new coordinates are legal inside the inventory (send event to the listener if they are at all within inventory?)
                    if ((posX - 25) >= prePosX || (posX + 25) <= prePosX || (posY - 25) >= prePosY || (posY + 25) <= prePosY) {
                        prePosX = ((posX - 25) >= prePosX || (posX + 25) <= prePosX) ? posX : prePosX;
                        prePosY = ((posY - 25) >= prePosY || (posY + 25) <= prePosY) ? posY : prePosY;
                        setAcceptButtonState(posX, posY, itemImage.getRotation(), item);
                    }

                    /*if (getX() >= xGroupMin && getX() <= xGroupMax
                            && getY() >= yGroupMin && getY() <= yGroupMax) {

                    } else
                        acceptButton.setColor(100, 100, 100, 0.5f);*/
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
            discardButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent evt, float x, float y, int index, int button) {
                    clearChildren();
                    if (chosenImage != null)
                        chosenImage.setColor(1, 1, 1, 1);
                    return true;
                }
            });
            acceptButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent evt, float x, float y, int index, int button) {
                    if (acceptButtonStatus) {
                        if (isFromStorage) {
                            for (InventoryManagementListener iml : listeners) {
                                iml.storageItemDropped(item.getName(), (int) posX / 25,
                                        (int) posY / 25, (int) itemImage.getRotation() / 90);
                            }
                            return true;
                        } else {
                            for (InventoryManagementListener iml : listeners) {
                                iml.inventoryItemMoved(item, (int) posX / 25,
                                        (int) posY / 25, (int) itemImage.getRotation() / 90);
                            }
                            return true;
                        }
                    }
                    return false;
                }

            });
        }

        private void setAcceptButtonState(float x, float y, float rotation, Item item) {
            if (inventory.itemLegalAt((int) x / 25, (int) y / 25, (int) itemImage.getRotation() / 90 % 4, item)) {
                acceptButton.setColor(0, 0, 0, 1);
                acceptButtonStatus = true;
            } else {
                acceptButton.setColor(100, 100, 100, 0.5f);
                acceptButtonStatus = false;
            }
        }
    }
}
