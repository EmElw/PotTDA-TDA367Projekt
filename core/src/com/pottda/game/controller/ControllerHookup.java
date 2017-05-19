package com.pottda.game.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pottda.game.model.Character;
import com.pottda.game.model.ModelActor;
import com.pottda.game.model.NewModelListener;
import com.pottda.game.model.Obstacle;
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

    /**
     * The stage onto which the ViewActors are put
     */
    private final Stage stage;

    public ControllerHookup(Stage stage) {
        this.stage = stage;
    }

    private List<NewControllerListener> listenerList = new ArrayList<NewControllerListener>();

    public void addListener(NewControllerListener ncl) {
        listenerList.add(ncl);
    }

    @Override
    public void onNewModel(ModelActor m) {

        ActorView view;

        // Determine what kind of controller
        AbstractController controller = null;
        if (m instanceof Obstacle) {
            view = new ActorView(m.sprite,((Vector2f)((Obstacle) m).size));
            controller = new ObstacleController(m, view);
        } else if (m instanceof Character) {
            view  = new ActorView(m.sprite);
            stage.addActor(view);
            if (m.team == 0) {
                controller = createController(m, view);

            }
        }
        if (controller != null) {
            notifyListeners(controller);
        }
    }

    private void notifyListeners(AbstractController c) {
        for (NewControllerListener ncl : listenerList) {
            ncl.onNewController(c);
        }
    }

    private AbstractController createController(ModelActor m, ActorView view) {
        switch (m.behaviour) {
            case PLAYER:
                return createInputController(m, view);
            case DUMB:
                return new DumbAIController(m, view);
        }
        throw new Error("Missing controller setup for behaviour: " + m.behaviour.toString());
    }

    private AbstractController createInputController(ModelActor m, ActorView view) {
        switch (ControllerOptions.controllerSettings) {
            case TOUCH_JOYSTICK:
                return new TouchJoystickController(m, view, ControllerOptions.joystickStage);
            case KEYBOARD_MOUSE:
                return new KeyboardMouseController(m, view, stage);
            case KEYBOARD_ONLY:
                return new KeyboardOnlyController(m, view);
        }
        throw new Error("ControllerOptions doesn't have any settings");
    }
}
