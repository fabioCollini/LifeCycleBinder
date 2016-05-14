package com.test;


import it.codingjam.lifecyclebinder.ObjectBinder;

import java.util.concurrent.Callable;


public class ActivityWithRetainedProvider$LifeCycleBinder extends ObjectBinder<ActivityWithRetainedProvider, ActivityWithRetainedProvider> {
    public ActivityWithRetainedProvider$LifeCycleBinder(String bundlePrefix) {
        super(bundlePrefix);
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
