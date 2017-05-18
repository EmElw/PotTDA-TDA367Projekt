package com.pottda.game.model;

import com.pottda.game.view.Sprites;

import javax.vecmath.Vector2f;
import java.util.List;

/**
 * Created by Magnus on 2017-05-18.
 */
public abstract class AbstractModelBuilder implements BuilderModel {

    private List<NewModelListener> listenerList;

    protected Sprites sprite;
    protected Vector2f postion;

    public AbstractModelBuilder() {

    }

    protected void setCommonParameters(ModelActor modelActor) {
        modelActor.sprite = sprite;
        modelActor.setPosition(postion);
    }

    @Override
    public BuilderModel setSprite(Sprites sprites) {
        this.sprite = sprites;
        return this;
    }

    @Override
    public BuilderModel setPosition(Vector2f position) {
        this.postion = position;
        return this;
    }

    protected void notifyListeners(ModelActor m) {
        for (NewModelListener nml : listenerList) {
            nml.onNewModel(m);
        }
    }
}
