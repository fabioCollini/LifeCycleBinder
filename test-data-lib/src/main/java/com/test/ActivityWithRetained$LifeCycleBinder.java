package com.test;

import it.codingjam.lifecyclebinder.ObjectBinder;

public final class ActivityWithRetained$LifeCycleBinder extends ObjectBinder<ActivityWithRetained, ActivityWithRetained> {
    public ActivityWithRetained$LifeCycleBinder() {
        super("com.test.ActivityWithRetained");
    }

    public void bind(ActivityWithRetained view) {
        initRetainedObject(bundlePrefix + "myName", view.myObject);
    }
}
