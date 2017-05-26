package com.pottda.game.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Created by Magnus on 2017-05-26.
 */
public class ControllerManager implements NewControllerListener {

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
    }

    public void update() {
        removeDeadActors();

        updateActors();

        addNewActors();
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
                controllerRemovalBuffer.add(controller);
            }
        }

        if (!controllerRemovalBuffer.isEmpty()) {
            controllers.removeAll(controllerRemovalBuffer);
            controllerRemovalBuffer.clear();
        }
    }
}
