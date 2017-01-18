package com.test.retainedObjectsWithProvider;


import com.test.MyObject;

import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;

import java.util.concurrent.Callable;

public class ActivityWithRetainedProvider$LifeCycleBinder {
    public static void bind(LifeCycleAwareCollector collector, final ActivityWithRetainedProvider view) {
        collector.addRetainedFactory("myObject", new Callable<MyObject>() {
            @Override
            public MyObject call() throws Exception {
                return view.myObject.get();
            }
        }, true);
    }
}
