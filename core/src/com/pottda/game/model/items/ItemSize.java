package com.pottda.game.model.items;


public enum ItemSize {
    MINOR ("Minor ", 0.5f, 1f, 0),
    NORMAL ("", 0.25f, 2f, 1),
    BIG ("Greater ", 0.1f, 4.5f, 2);

    private final String name;
    private final float dropRate;
    private final float statMultiplier;
    private final int identifier;

    ItemSize(String name, float dropRate, float statMultiplier, int identifier){
        this.name = name;
        this.dropRate = dropRate;
        this.statMultiplier = statMultiplier;
        this.identifier = identifier;
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

    public int getIdentifier() {
        return identifier;
    }

    public static ItemSize getSize(int identifier){
        for (ItemSize is : ItemSize.values()){
            if (is.getIdentifier() == identifier){
                return is;
            }
        }
        return null;
    }
}
