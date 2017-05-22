package com.pottda.game.model.items;


public enum ItemSize {
    MINOR ("Minor ", 0.5f, 1f),
    NORMAL ("", 0.25f, 2f),
    BIG ("Greater ", 0.1f, 4.5f);

    private final String name;
    private final float dropRate;
    private final float statMultiplier;
    ItemSize(String name, float dropRate, float statMultiplier){
        this.name = name;
        this.dropRate = dropRate;
        this.statMultiplier = statMultiplier;
    }

    public String getName() {
        return name;
    }

    public float getDropRate() {
        return dropRate;
    }

    public float getStatMultiplier() {
        return statMultiplier;
    }
}
