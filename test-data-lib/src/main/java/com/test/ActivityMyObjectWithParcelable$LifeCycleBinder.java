package com.test;

import android.os.Bundle;

import it.codingjam.lifecyclebinder.ObjectBinder;

public final class ActivityMyObjectWithParcelable$LifeCycleBinder extends ObjectBinder<ActivityMyObjectWithParcelable, ActivityMyObjectWithParcelable> {
    private MyObjectWithParcelable2$LifeCycleBinder myObject = new MyObjectWithParcelable2$LifeCycleBinder("com.test.ActivityMyObjectWithParcelable myObject");

    public ActivityMyObjectWithParcelable$LifeCycleBinder(String bundlePrefix) {
        super(bundlePrefix);
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
