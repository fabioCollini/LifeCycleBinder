package com.test.myActivityEvents;

import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;

public class MyActivity$LifeCycleBinder {

    public static void bind(LifeCycleAwareCollector collector, final MyActivity view) {
        MyObjectWithEvents$LifeCycleBinder.bind(collector, collector.getOrCreate(view.myObject, null, null), true);
    }
}
