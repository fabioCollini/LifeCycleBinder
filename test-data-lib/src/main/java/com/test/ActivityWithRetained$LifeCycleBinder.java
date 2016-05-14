package com.test;

import it.codingjam.lifecyclebinder.ObjectBinder;

public class ActivityWithRetained$LifeCycleBinder extends ObjectBinder<ActivityWithRetained, ActivityWithRetained> {
    public ActivityWithRetained$LifeCycleBinder(String bundlePrefix) {
        super(bundlePrefix);
    }

    public void bind(final ActivityWithRetained view) {
        initRetainedObject(bundlePrefix + "myName", view.myObject);
    }
}
