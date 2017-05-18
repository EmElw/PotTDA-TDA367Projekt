package com.pottda.game.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pottda.game.model.Character;
import com.pottda.game.model.ModelActor;
import com.pottda.game.model.NewModelListener;
import com.pottda.game.model.Obstacle;
import com.pottda.game.view.ActorView;

import java.util.List;

/**
 * Created by Magnus on 2017-05-18.
 */
public class ControllerHookup implements NewModelListener {


    private List<NewControllerListener> listenerList;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void onNewModel(ModelActor m) {

        ActorView view = new ActorView(m.sprite);
        stage.addActor(view);

        // Determine what kind of controller
        AbstractController controller;
        if (m instanceof Obstacle) {
            controller = new ObstacleController(m, view);
        } else if (m instanceof Character) {
            if (m.team == 0) {
                controller = createController(m, view);

            }
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
