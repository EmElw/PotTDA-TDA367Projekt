package com.pottda.game.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pottda.game.model.ModelActor;
import com.pottda.game.view.ActorView;
import com.pottda.game.view.JoysticksView;

import javax.vecmath.Vector2f;

class TouchJoystickController extends AbstractController {
    private final JoysticksView joysticksView;

    TouchJoystickController(ModelActor modelActor, ActorView actorView, Stage stage) {
        super(modelActor, actorView);
        joysticksView = new JoysticksView(stage);
    }

    @Override
        public void setInputVectors() {

        joysticksView.onNewFrame();

        movementVector.set(joysticksView.getMovementKnobX(),
                joysticksView.getMovementKnobY());

        Vector2f v = new Vector2f(joysticksView.getAttackKnobX(), joysticksView.getAttackKnobY());
        if (v.length() > 0) {
            v.normalize();
            attackVector.set(v.x, v.y);
        } else {
            attackVector.set(0, 0);
        }
    }
}
