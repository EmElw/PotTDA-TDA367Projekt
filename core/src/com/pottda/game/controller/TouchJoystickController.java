package com.pottda.game.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pottda.game.model.ModelActor;
import com.pottda.game.view.JoysticksView;
import com.pottda.game.view.ActorView;

/**
 * Created by Rikard Teodorsson on 2017-04-07.
 */

public class TouchJoystickController extends AbstractController {
    private final JoysticksView joysticksView;

    public TouchJoystickController(ModelActor modelActor, ActorView actorView, Stage stage) {
        super(modelActor, actorView);
        joysticksView = new JoysticksView(stage);
    }

    @Override
    public void setInputVectors() {
        joysticksView.onNewFrame();
        movementVector.set(joysticksView.getMovementKnobX() * SPEED_MULT, joysticksView.getMovementKnobY() * SPEED_MULT);
        attackVector.set(joysticksView.getAttackKnobX(), joysticksView.getAttackKnobY());
        super.onNewFrame();
    }
}
