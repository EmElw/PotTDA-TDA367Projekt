package com.pottda.game.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pottda.game.model.Character;
import com.pottda.game.model.*;
import com.pottda.game.view.ActorView;
import com.pottda.game.view.ObstacleTextureFactory;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.List;

/**
 * A class which implements NewModelListener
 * <p>
 * On new model, it creates a new controller and sets its view and model
 */
public class ControllerHookup implements NewModelListener {

    private static final float HEALTHBAR_WIDTH = 1;
    private static final float HEALTHBAR_HEIGHT = 0.2f;
    private final Stage gameStage;
    private final Stage hudStage;

    public ControllerHookup(Stage gameStage, Stage hudStage) {
        this.gameStage = gameStage;
        this.hudStage = hudStage;
    }

    private final List<NewControllerListener> listenerList = new ArrayList<NewControllerListener>();

    public void addListener(NewControllerListener ncl) {
        listenerList.add(ncl);
    }

    @Override
    public void onNewModel(ModelActor m) {
        ActorView view = null;
        EnemyHealthBarController enemyHealthBarController = null;

        AbstractController controller = null;
        if (m instanceof Projectile) {
            view = new ActorView(m.sprite);
            controller = new ProjectileController(m, view);
        } else if (m instanceof Character) {
            view = new ActorView(m.sprite);
            if (m.team == Character.PLAYER_TEAM) {
                controller = createInputController(m, view);
            } else if (m.team == Character.ENEMY_TEAM) {
                enemyHealthBarController = new EnemyHealthBarController(HEALTHBAR_WIDTH, HEALTHBAR_HEIGHT, ((Character) m).getMaxHealth());
                controller = createController(m, view, enemyHealthBarController);
            } else {
                throw new Error("bad team");
            }
        } else if (m instanceof Obstacle) {
            if (((Obstacle) m).isRound) {
                Vector2f size = new Vector2f(((Obstacle) m).size.x, ((Obstacle) m).size.x);
                size.scale(2);
                view = new ActorView(ObstacleTextureFactory.getCircularObstacleTexture(((Obstacle) m).size.x), size);
            } else {
                view = new ActorView(ObstacleTextureFactory.getRectangularObstacleTexture((Vector2f) ((Obstacle) m).size), ((Vector2f) ((Obstacle) m).size));
            }
            controller = new ObstacleController(m, view);
        }

        try {
            gameStage.addActor(view);
            if (enemyHealthBarController != null) {
                gameStage.addActor(enemyHealthBarController.getRedView());
                gameStage.addActor(enemyHealthBarController.getFrameView());
            }
            notifyListeners(controller);
        } catch (NullPointerException e) {
            throw new Error("couldn't handle model-type", e);
        }
    }

    private void notifyListeners(AbstractController c) {
        for (NewControllerListener ncl : listenerList) {
            ncl.onNewController(c);
        }
    }

    private AbstractController createController(ModelActor m, ActorView view, EnemyHealthBarController enemyHealthBarController) {
        switch (m.behaviour) {
            case NONE:
                break;
            case DUMB:
                return new DumbAIController(m, view, enemyHealthBarController);
            case STATIONARY:
                return new StationaryAIController(m, view, enemyHealthBarController);
            case FIXATING:
                return new FixatingAIController(m, view, enemyHealthBarController);
        }
        throw new Error("Missing controller setup for behaviour: " + m.behaviour.toString());
    }

    private AbstractController createInputController(ModelActor m, ActorView view) {
        switch (ControllerOptions.controllerSettings) {
            case TOUCH_JOYSTICK:
                return new TouchJoystickController(m, view, hudStage);
            case KEYBOARD_MOUSE:
                return new KeyboardMouseController(m, view, gameStage);
            case KEYBOARD_ONLY:
                return new KeyboardOnlyController(m, view);
        }
        throw new Error("ControllerOptions doesn't have any settings");
    }
}
