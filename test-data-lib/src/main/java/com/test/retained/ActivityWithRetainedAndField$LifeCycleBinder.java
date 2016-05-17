package com.test.retained;

import it.codingjam.lifecyclebinder.ObjectBinder;

public class ActivityWithRetainedAndField$LifeCycleBinder extends ObjectBinder<ActivityWithRetainedAndField, ActivityWithRetainedAndField> {
    public ActivityWithRetainedAndField$LifeCycleBinder(String bundlePrefix) {
        super(bundlePrefix);
    }

    public void bind(final ActivityWithRetainedAndField view) {
        view.myObject = initRetainedObject(bundlePrefix + "myObjectProvider", view.myObjectProvider);
    }
}
