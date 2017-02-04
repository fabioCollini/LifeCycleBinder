package com.test.retainedEvents;

import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;

public class ActivityWithRetainedAndField$LifeCycleBinder {
    public static void bind(LifeCycleAwareCollector collector, final ActivityWithRetainedAndField view) {
        view.myObject = MyObjectWithEvents2$LifeCycleBinder.bind(collector, collector.getOrCreate(null, "myObjectProvider", view.myObjectProvider), true);
    }
}
