package com.test.retainedEvents;

import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;

public class ActivityWithRetained$LifeCycleBinder {
    public static void bind(LifeCycleAwareCollector<? extends ActivityWithRetained> collector, final ActivityWithRetained view) {
        MyObjectWithEvents1$LifeCycleBinder.bind(collector, collector.addRetainedFactory("myObjectProvider", view.myObjectProvider, false));
    }
}
