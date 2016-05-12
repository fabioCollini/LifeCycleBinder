package com.test;

import android.os.Bundle;

import it.codingjam.lifecyclebinder.ObjectBinder;

public final class ActivityMyObjectWithParcelableAndInnerObject$LifeCycleBinder extends ObjectBinder<ActivityMyObjectWithParcelableAndInnerObject, ActivityMyObjectWithParcelableAndInnerObject> {
    private MyObjectWithParcelableAndInnerObject$LifeCycleBinder myObject = new MyObjectWithParcelableAndInnerObject$LifeCycleBinder();

    public void bind(ActivityMyObjectWithParcelableAndInnerObject view) {
        listeners.add(view.myObject);
        myObject.bind(view.myObject);
        listeners.addAll(myObject.getListeners());
        retainedObjectCallables.putAll(myObject.getRetainedObjectCallables());
    }

    public void saveInstanceState(ActivityMyObjectWithParcelableAndInnerObject view, Bundle bundle) {
        myObject.saveInstanceState(view.myObject, bundle);
    }

    public void restoreInstanceState(ActivityMyObjectWithParcelableAndInnerObject view, Bundle bundle) {
        myObject.restoreInstanceState(view.myObject, bundle);
    }
}
