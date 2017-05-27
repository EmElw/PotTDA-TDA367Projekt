package com.pottda.game.application;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.pottda.game.controller.ControllerOptions;
import com.pottda.game.model.Character;
import com.pottda.game.model.DeathListener;
import com.pottda.game.model.Inventory;
import com.pottda.game.model.Item;
import com.pottda.game.model.ScoreChangeListener;
import com.pottda.game.model.Sprites;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class HUDStage extends Stage implements ScoreChangeListener {

    private List<ItemDropLabel> itemDropLabelList;
    private Label.LabelStyle labelStyle;
    private static final float labelMargin = 3f;
    private static final long WAITING_TIME_LABEL_SECONDS = 3;

    private Label scoreLabel;
    private static final String scoreLabelText = "Score: ";

    private Inventory inventory;

    private int score = 0;

    private Image healthBarRed;

    private boolean toPause = false;

    HUDStage(StretchViewport stretchViewport) {
        super((stretchViewport));
    }

    void initStage() {
        itemDropLabelList = new ArrayList<ItemDropLabel>();

        BitmapFont bf = new BitmapFont();
        labelStyle = new Label.LabelStyle(bf, Color.WHITE);
        scoreLabel = new Label(scoreLabelText, labelStyle);
        scoreLabel.setPosition(getWidth() / 6, getHeight() - 30);
        scoreLabel.setFontScale(1.5f);
        addActor(scoreLabel);

        addActors();
    }

    private void addActors() {
        Image healthBar = new Image(new Texture(Gdx.files.internal(Sprites.HEALTHBAR.fileName)));
        healthBar.setPosition(10, getHeight() - 30);
        addActor(healthBar);

        healthBarRed = new Image(new Texture(Gdx.files.internal(Sprites.HEALTHBARRED.fileName)));
        healthBarRed.setPosition(10, getHeight() - 30);
        addActor(healthBarRed);

        Image pauseButton = new Image(new Texture(Gdx.files.internal(Sprites.PAUSEBUTTON.fileName)));
        pauseButton.setPosition(getWidth() - 60, getHeight() - 50);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toPause = true;
            }
        });
        addActor(pauseButton);
    }

    @Override
    public void draw() {
        inventory = Character.player.inventory;

        setHealthbar();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            toPause = true;
        }

        updateLabels();
        super.draw();
    }

    private void setHealthbar() {
        float health = Character.player.getCurrentHealth();
        final float playerMaxHealth = Character.player.getMaxHealth();

        if (health < 0) {
            health = 0;
        }
        healthBarRed.setWidth((health / playerMaxHealth) * 100f);
    }

    private void addItemLabel(String name) {
        Label label = new Label(name, labelStyle);
        addActor(label);
        itemDropLabelList.add(new ItemDropLabel(label, System.currentTimeMillis()));
    }

    private void updateLabels() {
        for (int i = 0; i < itemDropLabelList.size(); i++) {
            ItemDropLabel itemDropLabel = itemDropLabelList.get(i);
            final boolean isFadingOut = itemDropLabel.isFadingOut();
            final boolean isFadingIn = itemDropLabel.isFadingIn();
            final Label label = itemDropLabel.getLabel();
            final long time = itemDropLabel.getTimeSinceAppeared();

            if (!isFadingOut && (System.currentTimeMillis() - time) / 1000 > WAITING_TIME_LABEL_SECONDS) {
                itemDropLabel.setFadeOut(true);
            }
            if (isFadingIn) {
                Color color = label.getColor();
                if (color.a < 1f) {
                    label.setColor(color.r, color.g, color.b, color.a + 0.01f);
                } else {
                    itemDropLabel.setFadeIn(false);
                }
            } else if (isFadingOut) {
                Color color = label.getColor();
                if (color.a > 0f) {
                    label.setColor(color.r, color.g, color.b, color.a - 0.01f);
                } else {
                    // Delete once faded out
                    itemDropLabelList.remove(i);
                }
            }
            if (ControllerOptions.controllerSettings == ControllerOptions.ControllerMode.TOUCH_JOYSTICK) {
                // Center the labels when using joysticks to prevent fingers/joystick to cover the text
                label.setPosition(getWidth() / 2 - label.getPrefWidth() / 2, label.getPrefHeight() * i);
            } else {
                label.setPosition(getWidth() - label.getPrefWidth() - labelMargin, label.getPrefHeight() * i);
            }
        }
    }

    void onNewDrop(Set<Item> droppedItems) {
        for (Item i : droppedItems) {
            if (i != null) {
                addItemLabel(i.getName());
            }
        }
    }

    boolean toPause() {
        return toPause;
    }

    void setToPause(boolean toPause) {
        this.toPause = toPause;
    }

    @Override
    public void scoreChanged(int points) {
        score += points;
        scoreLabel.setText(scoreLabelText + score);
    }
}
