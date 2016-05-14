package com.test;

import it.codingjam.lifecyclebinder.ObjectBinder;

public class ActivityWithRetained2$LifeCycleBinder extends ObjectBinder<ActivityWithRetained2, ActivityWithRetained2> {
    public ActivityWithRetained2$LifeCycleBinder(String bundlePrefix) {
        super(bundlePrefix);
    }

    public void bind(final ActivityWithRetained2 view) {
        initRetainedObject(bundlePrefix + "myName", view.myObject);
        initRetainedObject(bundlePrefix + "myName2", view.myObject2);
    }
}
