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

    Label getLabel() {
        return label;
    }

    long getTimeSinceAppeared() {
        return timeSinceAppeared;
    }

    void setFadeOut(boolean fadeOut) {
        this.fadeOut = fadeOut;
    }

    void setFadeIn(boolean fadeIn) {
        this.fadeIn = fadeIn;
    }

    boolean isFadingIn() {
        return fadeIn;
    }

    boolean isFadingOut() {
        return fadeOut;
    }
}
