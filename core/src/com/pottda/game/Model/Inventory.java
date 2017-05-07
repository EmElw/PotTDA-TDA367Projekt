package com.pottda.game.model;

import javax.vecmath.Vector2f;
import java.util.*;

/**
 * An Inventory, containing {@link Item}.
 * <p>
 * Responsible for interpreting the properties and connections
 * of {@code Item}s as necessary
 */

public class Inventory {
    /*
    Starting points, set when compule() is called
     */
    public final Set<AttackItem> attackItems;

//    public final List<SupportItem> supportItems;
//    public final List<Item> inactiveItems;

    /*
    A list of the Items in this Inventory
     */
    public final List<Item> items;
    /*
    A map kept to quickly find what Item is at a given position, if any
     */
    public final Map<Integer, Item> positionMap;

    private int height;
    private int width;

    // Should be called after creation and when the inventory's state is changed
    public void compile() {
        attackItems.clear();
        for (Item i : items) {
            if (i.isAttackItem) {
                attackItems.add((AttackItem) i);
            }
        }
        // TODO check for circular loops
        // TODO check for unique items
    }

    /**
     * Attacks in the given direction
     *
     * @param direction a {@link Vector2f} in the wanted direction
     */
    public void attack(Vector2f direction) {

        // Iterate through all attack items and do stuff
        for (AttackItem a : attackItems) {
            // Create a projectile listener list, copy for each projectile
            List<ProjectileListener> listeners = new ArrayList<ProjectileListener>();

            Item i = a;
                /*
                Iterate through the items until there is no item
                at the output position

                (Map.get(Key) returns "null" if there's no key (although
                it can also return null if the Value is "null"))
                 */
            while ((i = positionMap.get(i.getOutputAsInteger(width))) != null) {
                if (i.isProjectileModifier) {   // Modifiers need to listen to projectile events
                    listeners.add(i);
                }
                if (i.isAttackItem || i.isSecondaryAttackItem) {
                    // Attack and SecondaryAttacks doesn't propagate the chain further
                    break;
                }
            }
        }
    }

    public Inventory() {
        attackItems = new HashSet<AttackItem>();
//        supportItems = new ArrayList<SupportItem>();
//        inactiveItems = new ArrayList<Item>();
        items = new ArrayList<Item>();
        positionMap = new TreeMap<Integer, Item>();
    }

    public void setDimensions(int w, int h) {
        this.width = w;
        this.height = h;
    }

//    public int getCooldown() {
//        int cooldown = 0;
//        for (AttackItem attackItem : attackItems) {
//            cooldown += attackItem.cooldown;
//        }
//        return cooldown;
//    }
//
//    public int getHealth() {
//        int health = 0;
//        for (SupportItem supportItem : supportItems) {
//            health += supportItem.health;
//        }
//        return health;
//    }
//
//    public float getAcceleration() {
//        float acceleration = 0.0f;
//        for (SupportItem supportItem : supportItems) {
//            acceleration += supportItem.acceleration;
//        }
//        return acceleration;
//    }
//
//    public List<Projectile> getProjectile() {
//        List<Projectile> projectiles = new ArrayList<Projectile>(getProjectileAmount());
//        int damage = getDamage();
//        List<ProjectileListener> projectileListeners = getProjectileListeners();
//
//        for (int i = 0, n = getProjectileAmount(); i < n; i++) {
//            projectiles.add(new Projectile(new PhysicsActor() {
//                @Override
//                public Vector2f getPosition() {
//                    return null;
//                }
//            }, damage, projectileListeners));
//        }
//
//        return projectiles;
//    }
//
//    public void addItem(Item item) {
//        items.add(item);
//    }
//
//    private int getDamage() {
//        int damage = 0;
//        for (AttackItem attackItem : attackItems) {
//            damage += attackItem.damage;
//        }
//        return damage;
//    }
//
//    private List<ProjectileListener> getProjectileListeners() {
//        List<ProjectileListener> projectileListeners = new ArrayList<ProjectileListener>();
//        for (AttackItem attackItem : attackItems) {
//            if (attackItem.projectileListener != null) {
//                projectileListeners.add(attackItem.projectileListener);
//            }
//        }
//        return projectileListeners;
//    }
//
//    private int getProjectileAmount() {
//        int projectileAmount = 1;
//        for (AttackItem attackItem : attackItems) {
//            // Need to subtract by one so multiple Items that has one Projectile each makes the Character shoot
//            // multiple Projectiles.
//            projectileAmount += attackItem.projectileAmount - 1;
//        }
//        return projectileAmount;
//    }

}
