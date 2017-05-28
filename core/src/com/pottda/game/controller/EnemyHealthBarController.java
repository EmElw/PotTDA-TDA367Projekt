package com.pottda.game.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.pottda.game.model.Constants;
import com.pottda.game.view.ActorView;

import javax.vecmath.Vector2f;

public class EnemyHealthBarController {
    private int maxHealth;
    private final int width;
    private final int height;
    private int lastHealth;
    private ActorView frameView;
    private ActorView redView;

    public EnemyHealthBarController(float width, float height, int maxHealth) {
        this.width = Math.round(width / Constants.WIDTH_RATIO);
        this.height = Math.round(height / Constants.HEIGHT_RATIO);
        this.maxHealth = maxHealth;
        lastHealth = maxHealth;

        Pixmap frame = new Pixmap(this.width, this.height, Pixmap.Format.RGBA8888);
        frame.setColor(Color.WHITE);
        frame.drawRectangle(0, 0, this.width, this.height);

        Pixmap filling = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        filling.setColor(Color.RED);
        filling.fillRectangle(0, 0, 1, 1);

        Texture frameTexture = new Texture(frame);
        Texture redTexture = new Texture(filling);

        frame.dispose();
        filling.dispose();

        redView = new ActorView(redTexture, new Vector2f(width, height));
        frameView = new ActorView(frameTexture);
    }

    public void setHealth(int currentHealth) {
        if(currentHealth == maxHealth){
            redView.setVisible(false);
            frameView.setVisible(false);
        } else {
            redView.setVisible(true);
            frameView.setVisible(true);
        }

        if (currentHealth != lastHealth) {
            redView.setScale((currentHealth / ((float) maxHealth)), 1);
            lastHealth = currentHealth;
        }
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public ActorView getRedView() {
        return redView;
    }

    public ActorView getFrameView() {
        return frameView;
    }
}
