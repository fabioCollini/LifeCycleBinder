package com.test.myActivity;

import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;

public class MyActivity$LifeCycleBinder {

    public static void bind(LifeCycleAwareCollector collector, final MyActivity view) {
        MyObject$LifeCycleBinder.bind(collector, collector.getOrCreate(view.myObject, null, null), true);
    }
}
