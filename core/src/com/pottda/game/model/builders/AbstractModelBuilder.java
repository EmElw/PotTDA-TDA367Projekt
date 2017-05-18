package com.pottda.game.model.builders;

import com.pottda.game.model.ModelActor;
import com.pottda.game.model.NewModelListener;
import com.pottda.game.model.PhysicsActor;
import com.pottda.game.view.Sprites;

import javax.vecmath.Vector2f;
import java.util.List;

/**
 * Created by Magnus on 2017-05-18.
 */
public abstract class AbstractModelBuilder implements IModelBuilder {

    private List<NewModelListener> listenerList;

    protected PhysiscActorFactory physiscActorFactory;

    public void setPhysiscActorFactory(PhysicsActorFactory physiscActorFactory) {
        this.physiscActorFactory = physiscActorFactory;
    }

    protected Sprites sprite;
    protected Vector2f postion;

    public AbstractModelBuilder() {

    }

    protected void setCommonAndNotify(ModelActor modelActor) {
        modelActor.sprite = sprite;
        modelActor.setPosition(postion);
        notifyListeners(modelActor);
    }

    @Override
    public IModelBuilder setSprite(Sprites sprites) {
        this.sprite = sprites;
        return this;
    }

    @Override
    public IModelBuilder setPosition(Vector2f position) {
        this.postion = position;
        return this;
    }

    private void notifyListeners(ModelActor m) {
        for (NewModelListener nml : listenerList) {
            nml.onNewModel(m);
        }
    }
}