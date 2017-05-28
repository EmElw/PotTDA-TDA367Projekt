package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.pottda.game.model.Inventory;
import com.pottda.game.model.Item;
import com.pottda.game.model.Storage;

import javax.vecmath.Point2i;
import java.util.ArrayList;
import java.util.List;

import static com.pottda.game.application.Constants.SKIN_QH;
import static com.pottda.game.view.AtlasCreator.SIZE;
import static com.pottda.game.view.AtlasCreator.atlas;

/**
 * Created by Mr Cornholio on 15/05/2017.
 */
public class InventoryManagementView {
    private Stage stage;

    private Table storageSuperTable;
    private Table inventorySuperTable;

    private Skin mySkin = SKIN_QH;

    private WidgetGroup inventoryGroup;

    private WorkingImageTable currentWorkingItem;

    private Inventory inventory;

    private List<InventoryManagementListener> listeners;

    private Texture connection = new Texture(Gdx.files.internal("testconnection.png"));
    private Texture notConnection = new Texture(Gdx.files.internal("outputTest.png"));
    private Texture background = new Texture(Gdx.files.internal("bg/bg.png"));

    public InventoryManagementView(final Stage stage) {
        this.stage = stage;
        stage.setDebugAll(true);
        this.listeners = new ArrayList<InventoryManagementListener>();
        create();
    }

    public void create() {
        Gdx.input.setInputProcessor(stage);

        Table superTable = new Table();
        superTable.setFillParent(true);
        stage.addActor(superTable);

        // Create table to hold storage section
        storageSuperTable = new Table();
        storageSuperTable.setWidth(150);
        // Create storage & scrollpane for storage
        ScrollPane scroll = new ScrollPane(storageSuperTable);
        scroll.layout();
        scroll.setForceScroll(false, true);
        scroll.setOverscroll(false, false);
        Table storage = new Table();
        storage.add(scroll).height(stage.getHeight() - 25);

        // Create group to hold inventory section
        inventorySuperTable = new Table();

        // Create labels for storage and inventory
        Label storageLabel = new Label("Storage", mySkin);
        Label inventoryLabel = new Label("Inventory", mySkin);

        // Add labels and storage/inventory table
        superTable.add(storageLabel);
        superTable.add(inventoryLabel);
        superTable.row();
        superTable.add(storage).width(275);
        superTable.add(inventorySuperTable).fill().expand();
    }

    // Interesting stuff

    /**
     * Sets the current working item
     * <p>
     * Can be null, in which case there is no current working item
     *
     * @param workingImageTable
     */
    private void newWorkingItem(WorkingImageTable workingImageTable) {
        if (currentWorkingItem == null) {
            currentWorkingItem = workingImageTable;
        } else {
            if (currentWorkingItem.inventoryImage != null) {
                currentWorkingItem.inventoryImage.setTouchable(Touchable.enabled);
                currentWorkingItem.inventoryImage.setColor(1, 1, 1, 1);
            }
            currentWorkingItem.remove();
            currentWorkingItem = workingImageTable;
        }
        if (currentWorkingItem != null) {
            inventoryGroup.addActor(currentWorkingItem);
        }
    }

    public void updateStorageTable(Storage storage) {
        if (storageSuperTable != null)
            storageSuperTable.clearChildren();
        for (String s : storage.getItems()) {
            try {
                addToStorageTable(storage.getNrOf(s), storage.getItem(s));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addToStorageTable(int itemCount, final Item item) {
        // Create a table to hold name + image
        Button storageTable = new Button(mySkin);
        final StorageImage storageImage = new StorageImage(item);
        Stack storageStack = new Stack();
        Image itemImage;

        Table internalItemGroupTable = new Table();
        itemImage = new Image(atlas.findRegion(item.getName()));

        // Label hold items name
        Label itemNameLabel = new Label(item.getName(), mySkin);
        itemNameLabel.setFontScale(0.75f, 0.75f);
        internalItemGroupTable.add(itemNameLabel).left();
        internalItemGroupTable.row();

        // Label holds item count
        Label itemCountLabel = new Label("#" + itemCount, mySkin);
        itemCountLabel.setFontScale(0.5f, 0.5f);
        internalItemGroupTable.add(itemCountLabel).right();

        // Add a label and image to the table and fit the image
        storageTable.add(internalItemGroupTable).left().spaceRight(10);
        storageTable.add(itemImage);
        itemImage.setScaling(Scaling.fit);
        storageStack.add(storageImage);
        storageStack.add(storageTable);

        // Add touch-listener
        storageStack.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent evt, float x, float y, int index, int button) {
                newWorkingItem(new WorkingImageTable(item, 0, 0, 0, true));
                return true;
            }
        });

        // Add the table to our main storage table
        this.storageSuperTable.add(storageStack).fill();
        this.storageSuperTable.row();
    }

    public void updateInventoryGroup(Inventory inventory) {
        // Create inventory
        this.inventory = inventory;
        if (inventoryGroup != null)
            inventorySuperTable.clearChildren();
        inventoryGroup = new WidgetGroup();

        Image bg = new Image(new TextureRegion(background,
                SIZE * (inventory.getWidth() + 1),
                SIZE * (inventory.getHeight() + 1)));
        inventoryGroup.addActor(bg);


        List<Point2i> connections = new ArrayList<Point2i>();

        for (Item i : inventory.getItems()) {
            TextureAtlas.AtlasRegion region = atlas.findRegion(i.getName());
            final ItemImage itemImage = new ItemImage(region, i);

            final Point2i negativeOffset = i.getBaseBottomLeft();

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
                @Override
                public boolean touchDown(InputEvent evt, float x, float y, int index, int button) {
                    return true; // More complex bounding box goes here
                }

                @Override

                public void touchUp(InputEvent evt, float x, float y, int index, int button) {
                    WorkingImageTable workingItem = new WorkingImageTable(itemImage.item,
                            itemImage.getX() - negativeOffset.x * SIZE,
                            itemImage.getY() - negativeOffset.y * SIZE,
                            itemImage.getRotation(),
                            false);
                    newWorkingItem(workingItem);
                    workingItem.setInventoryImage(itemImage);
                    itemImage.setTouchable(Touchable.disabled);
                    itemImage.setColor(1, 1, 1, 0.5f);
                }
            });
        }

        for (Point2i p : connections) {
            Image connectionImage = new Image(
                    inventory.itemAt(p) == null ? notConnection : connection);

            connectionImage.setPosition(p.x * SIZE, p.y * SIZE);
            connectionImage.setTouchable(Touchable.disabled);
            inventoryGroup.addActor(connectionImage);
        }

        inventoryGroup.validate();
        inventorySuperTable.add(inventoryGroup).expand().bottom().left().pad(2 * SIZE);
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
            super();
//            super(new Texture(Gdx.files.internal("menu/storageButtonBackground.png")));
            this.item = item;
        }
    }

    private class ItemImage extends Image {
        private Item item;

        private ItemImage(TextureAtlas.AtlasRegion region, Item item) {
            super(region);
            this.item = item;
        }
    }

    private class WorkingImageTable extends WidgetGroup {

        private final Drawable rotateRightButtonDrawable = new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("rotateRightButton.png"))));
        private final Drawable rotateLeftButtonDrawable = new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("rotateLeftButton.png"))));
        private final Drawable acceptButtonDrawable = new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("acceptButton.png"))));
        private final Drawable discardButtonDrawable = new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("discardButton.png"))));
        private final Label debugLabel;
        private ImageButton rotateRightButton = new ImageButton(rotateRightButtonDrawable);

        private ImageButton rotateLeftButton = new ImageButton(rotateLeftButtonDrawable);
        private ImageButton acceptButton = new ImageButton(acceptButtonDrawable);
        private ImageButton discardButton = new ImageButton(discardButtonDrawable);
        private final Item workingItem;
        private final Image itemImage;

        private boolean currentPositionIsLegal = false;

        private final boolean isFromStorage;

        private Point2i inventoryPosition;
        private Point2i negativeOffset;
        private final Point2i negativeOffsetPx;
        private ItemImage inventoryImage;
        private int orientation;

        private WorkingImageTable(final Item item, float x, float y, float rotation, boolean isFromStorage) {
            this.workingItem = item;
            this.isFromStorage = isFromStorage;
            this.setPosition(x, y);
            this.orientation = (int) (rotation / 90);

            negativeOffset = item.getBaseBottomLeft();
            negativeOffsetPx = new Point2i(negativeOffset.x * SIZE, negativeOffset.y * SIZE);
            inventoryPosition = new Point2i(Math.round(x / SIZE), Math.round(y / SIZE));

            itemImage = new Image(atlas.findRegion(item.getName()));
            itemImage.setOrigin(
                    (float) ((0.5 - negativeOffset.x) * SIZE),
                    (float) ((0.5 - negativeOffset.y) * SIZE));
            itemImage.setPosition(negativeOffset.x * SIZE, negativeOffset.y * SIZE);
            addActor(itemImage);
            itemImage.setRotation(rotation);

            float d = Math.max(itemImage.getWidth(), itemImage.getHeight());
            this.setSize(d, d);

            discardButton.setSize(SIZE, SIZE);
            discardButton.setPosition(getWidth(), getHeight());
            addActor(discardButton);

            acceptButton.setSize(SIZE, SIZE);
            acceptButton.setPosition(-SIZE + negativeOffsetPx.x, getHeight());
            addActor(acceptButton);

            rotateLeftButton.setSize(SIZE, SIZE);
            rotateLeftButton.setPosition(-SIZE + negativeOffsetPx.x, -SIZE + negativeOffsetPx.y);
            addActor(rotateLeftButton);

            rotateRightButton.setSize(SIZE, SIZE);
            rotateRightButton.setPosition(getWidth(), -SIZE + negativeOffsetPx.y);
            addActor(rotateRightButton);

            inventoryGroup.addActor(this);

            initInputListeners();

            debugLabel = new Label("", mySkin);
            debugLabel.setPosition(itemImage.getOriginX(),
                    itemImage.getOriginY());

            addActor(debugLabel);

            updatePositionLegality();
        }

        private void updatePositionLegality() {
            debugLabel.setText(inventoryPosition.x + "/" + inventoryPosition.y + " : " + itemImage.getRotation() / 90);

            currentPositionIsLegal = inventory.itemLegalAt(inventoryPosition.x,
                    inventoryPosition.y,
                    (int) (itemImage.getRotation() / 90),
                    workingItem);
            acceptButton.setDisabled(currentPositionIsLegal);
            acceptButton.setColor(1, 1, 1, currentPositionIsLegal ? 1 : 0.4f);
        }

        private void initInputListeners() {
            addListener(new InputListener() {
                private Point2i newPoint = new Point2i();
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
                    float pxX = getX() + x - xOffset;
                    float pxY = getY() + y - yOffset;

                    newPoint.set(   // Magical expression to convert and clamp within inventory
                            Math.min(Math.max(Math.round(pxX / SIZE), 0), inventory.getWidth()),
                            Math.min(Math.max(Math.round(pxY / SIZE), 0), inventory.getHeight()));

                    if (!inventoryPosition.equals(newPoint)) {
                        inventoryPosition.set(newPoint);
                        setPosition(newPoint.x * SIZE,
                                newPoint.y * SIZE);
                        updatePositionLegality();
                    }
                }
            });

            rotateLeftButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent evt, float x, float y) {
                    orientation = (orientation + 1) % 4;
                    itemImage.setRotation(orientation * 90);
                    updatePositionLegality();
                }
            });
            rotateRightButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent evt, float x, float y) {
                    orientation = (orientation + 3) % 4; //Easier because of java negative modulo stuff
                    itemImage.setRotation(orientation * 90);
                    updatePositionLegality();
                }
            });
            discardButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent evt, float x, float y) {
                    newWorkingItem(null);
                }
            });
            acceptButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent evt, float x, float y) {
                    if (currentPositionIsLegal) {
                        if (isFromStorage) {
                            for (InventoryManagementListener iml : listeners) {
                                iml.storageItemToInventory(
                                        workingItem.getName(),
                                        inventoryPosition.x,
                                        inventoryPosition.y,
                                        orientation);
                            }
                        } else {
                            for (InventoryManagementListener iml : listeners) {
                                iml.inventoryItemMoved(workingItem,
                                        inventoryPosition.x,
                                        inventoryPosition.y,
                                        orientation);
                            }
                        }
                    } else throw new Error("shouldn't get here with disabled button");
                }
            });
        }

        private void setInventoryImage(ItemImage inventoryImage) {
            this.inventoryImage = inventoryImage;
        }
    }
}
