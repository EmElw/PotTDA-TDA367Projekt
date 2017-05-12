package com.pottda.game.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pottda.game.model.ModelActor;
import com.pottda.game.view.JoysticksView;
import com.pottda.game.view.ViewActor;

/**
 * Created by Rikard Teodorsson on 2017-04-07.
 */

public class TouchJoystickController extends AbstractController {
    private final JoysticksView joysticksView;

    public TouchJoystickController(ModelActor modelActor, ViewActor viewActor, Stage stage) {
        super(modelActor, viewActor);
        joysticksView = new JoysticksView(stage);
    }

    @Override
    public void onNewFrame() {
        joysticksView.onNewFrame();
        movementVector.set(joysticksView.getMovementKnobX(), joysticksView.getMovementKnobY());
        attackVector.set(joysticksView.getAttackKnobX(), -joysticksView.getAttackKnobY());
        super.onNewFrame();
    }
}
