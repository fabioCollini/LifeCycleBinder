package com.test;

import it.codingjam.lifecyclebinder.ObjectBinder;

public final class ActivityWithRetained2$LifeCycleBinder extends ObjectBinder<ActivityWithRetained2, ActivityWithRetained2> {
    public ActivityWithRetained2$LifeCycleBinder() {
        super("com.test.ActivityWithRetained2");
    }

    public void bind(ActivityWithRetained2 view) {
        retainedObjectCallables.put(bundlePrefix + "myName", view.myObject);
        retainedObjectCallables.put(bundlePrefix + "myName2", view.myObject2);
    }
}
