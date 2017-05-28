package com.pottda.game.controller;

import com.pottda.game.model.Character;
import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ActorView;

import javax.vecmath.Vector2f;

public abstract class AIController extends AbstractController {
    private static final float HEALTHBAR_OFFSET = 1;
    private final EnemyHealthBarController enemyHealthBarController;

    /**
     * {@inheritDoc}
     *
     * @param modelActor
     * @param actorView
     */
    AIController(ModelActor modelActor, ActorView actorView, EnemyHealthBarController enemyHealthBarController) {
        super(modelActor, actorView);
        this.enemyHealthBarController = enemyHealthBarController;
    }

    @Override
    public void onNewFrame() {
        super.onNewFrame();
        updateHealthbar();
    }

    private void updateHealthbar() {
        enemyHealthBarController.setHealth(((Character) modelActor).getCurrentHealth());
        Vector2f position = new Vector2f(modelActor.getPosition());
        position.set(position.x, position.y - HEALTHBAR_OFFSET);
        enemyHealthBarController.getFrameView().setPoint(position.x, position.y, false);
        position.set(enemyHealthBarController.getFrameView().getX(), enemyHealthBarController.getFrameView().getY());
        enemyHealthBarController.getRedView().setPoint(position.x, position.y, false);
    }

    public EnemyHealthBarController getEnemyHealthBarController() {
        return enemyHealthBarController;
    }
}
