package com.test.retained;

import it.codingjam.lifecyclebinder.ObjectBinder;

public class ActivityWithRetainedAndField$LifeCycleBinder extends ObjectBinder<ActivityWithRetainedAndField, ActivityWithRetainedAndField> {
    public void bind(final ActivityWithRetainedAndField view) {
        view.myObject = initRetainedObject("myObjectProvider", view.myObjectProvider);
    }
}
