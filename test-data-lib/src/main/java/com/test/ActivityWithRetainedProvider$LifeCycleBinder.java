package com.test;


import java.util.concurrent.Callable;

import it.codingjam.lifecyclebinder.ObjectBinder;

public final class ActivityWithRetainedProvider$LifeCycleBinder extends ObjectBinder<ActivityWithRetainedProvider, ActivityWithRetainedProvider> {
    public ActivityWithRetainedProvider$LifeCycleBinder() {
        super("com.test.ActivityWithRetainedProvider");
    }

    public void bind(final ActivityWithRetainedProvider view) {
        initRetainedObject(bundlePrefix + "myName", new Callable<MyObject>() {
            @Override
            public MyObject call() throws Exception {
                return view.myObject.get();
            }
        });
    }
}
