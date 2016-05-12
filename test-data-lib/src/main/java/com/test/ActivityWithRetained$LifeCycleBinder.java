package com.test;

import it.codingjam.lifecyclebinder.ObjectBinder;

public final class ActivityWithRetained$LifeCycleBinder extends ObjectBinder<ActivityWithRetained, ActivityWithRetained> {
    public void bind(ActivityWithRetained view) {
        retainedObjectCallables.put("myName", view.myObject);
    }
}
