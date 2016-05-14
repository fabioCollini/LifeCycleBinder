package com.test;

import it.codingjam.lifecyclebinder.ObjectBinder;

public final class ActivityWithRetained2$LifeCycleBinder extends ObjectBinder<ActivityWithRetained2, ActivityWithRetained2> {
    public ActivityWithRetained2$LifeCycleBinder() {
        super("com.test.ActivityWithRetained2");
    }

    public void bind(ActivityWithRetained2 view) {
        initRetainedObject(bundlePrefix + "myName", view.myObject);
        initRetainedObject(bundlePrefix + "myName2", view.myObject2);
    }
}
