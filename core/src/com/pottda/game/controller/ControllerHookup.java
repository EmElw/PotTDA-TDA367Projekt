package com.pottda.game.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pottda.game.model.*;
import com.pottda.game.model.Character;
import com.pottda.game.view.ActorView;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.List;

/**
 * A class which implements NewModelListener
 * <p>
 * On new model, it creates a new controller and sets its view and model
 */
public class ControllerHookup implements NewModelListener {

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

        AbstractController controller = null;
        if (m instanceof Projectile) {
            view = new ActorView(m.sprite);
            controller = new ProjectileController(m, view);
        } else if (m instanceof Character) {
            view = new ActorView(m.sprite);
            if (m.team == Character.PLAYER_TEAM) {
                controller = createInputController(m, view);
            } else if (m.team == Character.ENEMY_TEAM) {
                controller = createController(m, view);
            } else {
                throw new Error("bad team");
            }
        } else if (m instanceof Obstacle) {
            if (((Obstacle) m).isRound) {
                int radius = Math.round(((Obstacle) m).size.x / Constants.WIDTH_RATIO);

                Pixmap tempPixMap = new Pixmap(radius * 2 + 1, radius * 2 + 1, Pixmap.Format.RGBA8888);

                tempPixMap.setColor(Color.BLACK);
                tempPixMap.fillCircle(radius, radius, radius);
                tempPixMap.setColor(Color.WHITE);
                tempPixMap.drawCircle(radius, radius, radius);

                Texture tempTexture = new Texture(tempPixMap);

                Vector2f size = new Vector2f(((Obstacle) m).size.x, ((Obstacle) m).size.x);
                size.scale(2);
                view = new ActorView(tempTexture, size);
            } else {
                int width = Math.round(((Obstacle) m).size.x / Constants.WIDTH_RATIO);
                int height = Math.round(((Obstacle) m).size.y / Constants.HEIGHT_RATIO);

                Pixmap tempPixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

                tempPixmap.setColor(Color.BLACK);
                tempPixmap.fillRectangle(0, 0, width, height);
                tempPixmap.setColor(Color.WHITE);
                tempPixmap.drawRectangle(0, 0, width, height);

                Texture tempTexture = new Texture(tempPixmap);

                view = new ActorView(tempTexture, ((Vector2f) ((Obstacle) m).size));
            }
            controller = new ObstacleController(m, view);
        }

        try {
            gameStage.addActor(view);
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

    private AbstractController createController(ModelActor m, ActorView view) {
        switch (m.behaviour) {
            case NONE:
                break;
            case DUMB:
                return new DumbAIController(m, view);
            case STATIONARY:
                return new StationaryAIController(m, view);
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
