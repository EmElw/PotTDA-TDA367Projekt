package com.pottda.game.model;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.List;


public class Projectile extends ModelActor {
    int damage;
    public boolean isBouncy = false;
    public boolean isPiercing = false;
    public final long timeOfConstructionMS;
    public long lifeTimeMS;
    static final int DEFAULT_PROJECTILE_LIFETIME_MS = 10000;
    /**
     * Listeners that care about various game-oriented events
     */
    private List<ProjectileListener> listeners;
    /**
     * A list synced with listeners.
     * <p>
     * If the equivalent entry on this list is true, the equivalent listener is not notified.
     * This is useful for things that should only occur once, such as the splitting of
     * projectiles with {@link com.pottda.game.model.items.MultiShot}.
     * <p>
     * (Supposedly better performance-wise)
     */
    private List<Boolean> ignored;
    //public List<Character> hasDamaged;

    public void addListener(ProjectileListener listener) {
        listeners.add(listener);
        ignored.add(false);
    }

    public void setListeners(List<ProjectileListener> listeners) {
        this.listeners = listeners;
        this.ignored = new ArrayList<Boolean>(listeners.size());
        for (int i = 0; i < listeners.size(); i++) {
            ignored.add(false);
        }
    }

    public void setListeners(List<ProjectileListener> listeners, List<Boolean> ignored) {
        this.listeners = listeners;
        this.ignored = ignored;
    }

    /**
     * Ignores the {@link ProjectileListener} so that it is not
     * notified of this projectile's events
     *
     * @param listener the {@link ProjectileListener} to ignore
     */
    public void ignoreListener(ProjectileListener listener) {
        ignored.set(listeners.indexOf(listener), true);
    }

    public List<Boolean> getIgnored() {
        return ignored;
    }

    /**
     * Ignores the {@link ProjectileListener} at the given index
     * so that it is not notified of this projectile's events
     *
     * @param index the index to ignore
     */
    public void ignoreListener(int index) {
        ignored.set(index, true);
    }

    public List<ProjectileListener> getListeners() {
        return listeners;
    }


    public Projectile(PhysicsActor physicsActor, int damage, List<ProjectileListener> listeners, Long lifeTimeMS) {
        super(physicsActor);
        this.damage = damage;
        this.listeners = listeners;
        timeOfConstructionMS = System.currentTimeMillis();
        this.lifeTimeMS = lifeTimeMS;
        //hasDamaged = new ArrayList<Character>();
    }

    public Projectile(PhysicsActor physicsActor, int damage, List<ProjectileListener> listeners) {
        super(physicsActor);
        this.damage = damage;
        this.listeners = listeners;
        timeOfConstructionMS = System.currentTimeMillis();
        lifeTimeMS = DEFAULT_PROJECTILE_LIFETIME_MS;
        //hasDamaged = new ArrayList<Character>();
    }

    /*@Override
    public VectorType getMove() {
        return null;
    }*/
    @Override
    public void giveInput(Vector2f movementVector, Vector2f attackVector) {
        if (isDying()) {
            return;
        }
        if (movementVector.length() == 0) {
            return;
        }
        physicsActor.giveMovementVector(movementVector);
        this.angle = (float) Math.toDegrees(Math.atan2(
                movementVector.getY(),
                movementVector.getX()));

    }

    @Override
    public float getAngle() {
        return angle;
    }

    public void onCollision(Character target) {
        target.takeDamage(damage);
        for (int i = 0; i < listeners.size(); i++) {
            if (!ignored.get(i)) {
                listeners.get(i).onHit(this);
            }
        }
        if (!isPiercing) {
            onCollision();
        }
    }

    public void onCollision() {
        if (!isBouncy) {
            shouldBeRemoved = true;
            onDestruction();
        }
    }

    private boolean isDying() {
        if (System.currentTimeMillis() - timeOfConstructionMS > lifeTimeMS) {
            shouldBeRemoved = true;
            onDestruction();
        }
        return shouldBeRemoved;
    }

    /**
     * Needs to be called after a projectile is a created and after
     * it has its listeners assigned
     */
    public void onAttack() {
        for (int i = 0; i < listeners.size(); i++) {
            if (!ignored.get(i)) {
                listeners.get(i).onAttack(this);
            }
        }
    }

    private void onDestruction() {
        for (int i = 0; i < listeners.size(); i++) {
            if (!ignored.get(i)) {
                listeners.get(i).onDestruction(this);
            }
        }
    }
}
