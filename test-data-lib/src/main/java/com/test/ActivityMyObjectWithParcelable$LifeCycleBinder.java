package com.test;

import android.os.Bundle;

import it.codingjam.lifecyclebinder.ObjectBinder;

public class ActivityMyObjectWithParcelable$LifeCycleBinder extends ObjectBinder<ActivityMyObjectWithParcelable, ActivityMyObjectWithParcelable> {
    private MyObjectWithParcelable2$LifeCycleBinder myObject;

    public ActivityMyObjectWithParcelable$LifeCycleBinder(String bundlePrefix) {
        super(bundlePrefix);
        myObject = new MyObjectWithParcelable2$LifeCycleBinder(bundlePrefix + SEPARATOR + "myObject");
    }

    public void bind(final ActivityMyObjectWithParcelable view) {
        listeners.add(view.myObject);
        myObject.bind(view.myObject);
        listeners.addAll(myObject.getListeners());
    }

    public void saveInstanceState(ActivityMyObjectWithParcelable view, Bundle bundle) {
        myObject.saveInstanceState(view.myObject, bundle);
    }

    public void restoreInstanceState(ActivityMyObjectWithParcelable view, Bundle bundle) {
        myObject.restoreInstanceState(view.myObject, bundle);
    }
}
