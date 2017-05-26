package com.pottda.game.application;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

class ItemDropLabel {
    private final Label label;
    private final long timeSinceAppeared;
    private boolean fadeOut;
    private boolean fadeIn;

    ItemDropLabel(Label label, long timeSinceAppeared) {
        this.label = label;
        this.timeSinceAppeared = timeSinceAppeared;
        fadeIn = true;
        fadeOut = false;
        label.setColor(label.getColor().r, label.getColor().g, label.getColor().b, 0f);
    }

    public Label getLabel() {
        return label;
    }

    public long getTimeSinceAppeared() {
        return timeSinceAppeared;
    }

    public void setFadeOut(boolean fadeOut) {
        this.fadeOut = fadeOut;
    }

    public void setFadeIn(boolean fadeIn) {
        this.fadeIn = fadeIn;
    }

    public boolean isFadingIn() {
        return fadeIn;
    }

    public boolean isFadingOut() {
        return fadeOut;
    }
}
