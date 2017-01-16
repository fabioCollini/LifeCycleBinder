package com.test.myActivityEvents;

import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;

public class MyActivity$LifeCycleBinder {

    public static void bind(LifeCycleAwareCollector<? extends MyActivity> collector, final MyActivity view) {
        MyObjectWithEvents$LifeCycleBinder.bind(collector, view.myObject);
    }
}
