package com.pottda.game.model.builders;

import com.pottda.game.model.ModelActor;
import com.pottda.game.model.NewModelListener;
import com.pottda.game.model.PhysicsActorFactory;
import com.pottda.game.model.Sprites;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magnus on 2017-05-18.
 */
public abstract class AbstractModelBuilder implements IModelBuilder {

    private static List<NewModelListener> listenerList = new ArrayList<NewModelListener>();

    protected static PhysicsActorFactory physiscActorFactory;

    public static void setPhysiscActorFactory(PhysicsActorFactory paf) {
        physiscActorFactory = paf;
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

    public static void addListener(NewModelListener nml){
        listenerList.add(nml);
    }

    public static void clear(){
        listenerList.clear();
    }
}
