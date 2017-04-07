package com.pottda.game.Model;

import java.util.List;

/**
 * Created by rikar on 2017-04-07.
 */

public class Inventory {
    // List<AttackItem> attackItems;
    // List<SupportItem> supportItems;
    List<Item> inactiveItems;

    public int getCooldown() {
        return 0;
    }

    public int getHealth() {
        return 0;
    }

    public float getAcceleration() {
        return 0;
    }

    public Projectile getProjectile() {
        return null;
    }

    public void addItem(Item item) {

    }
}
