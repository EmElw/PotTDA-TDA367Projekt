package com.pottda.game.controller;

import com.pottda.game.model.Character;
import com.pottda.game.model.ModelActor;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Created by Magnus on 2017-05-26.
 */
public class ControllerManager implements NewControllerListener {

    private AbstractController playerController;

    private Set<AbstractController> controllers;
    private Stack<AbstractController> controllerBuffer;
    private Stack<AbstractController> controllerRemovalBuffer;

    public ControllerManager() {
        this.controllers = new HashSet<AbstractController>();
        this.controllerBuffer = new Stack<AbstractController>();
        this.controllerRemovalBuffer = new Stack<AbstractController>();
    }

    @Override
    public void onNewController(AbstractController c) {
        controllerBuffer.push(c);
        if (c.getModel() instanceof Character) {
            if (c.getModel().team == ModelActor.PLAYER_TEAM) {
                if (playerController == null) {
                    playerController = c;
                } else throw new Error("Created another player");
            }
        }

    }

    public void updateControllers() {
        addNewActors();

        updateActors();

        removeDeadActors();
    }

    private void updateActors() {
        for (AbstractController c : controllers) {
            c.onNewFrame();
        }
    }

    private void addNewActors() {
        controllers.addAll(controllerBuffer);
        controllerBuffer.clear();
    }

    private void removeDeadActors() {
        for (AbstractController controller : controllers) {
            if (controller.shouldBeRemoved()) {
                controller.getModel().getPhysicsActor().destroyBody();
                controller.getView().remove();

                try {
                    ((AIController) controller).getEnemyHealthBarController().getRedView().remove();
                    ((AIController) controller).getEnemyHealthBarController().getFrameView().remove();
                } catch (ClassCastException e) {
                }

                controllerRemovalBuffer.add(controller);
            }
        }

        if (!controllerRemovalBuffer.isEmpty()) {
            controllers.removeAll(controllerRemovalBuffer);
            controllerRemovalBuffer.clear();
        }
    }

    public AbstractController getPlayerController() {
        return playerController;
    }
}
