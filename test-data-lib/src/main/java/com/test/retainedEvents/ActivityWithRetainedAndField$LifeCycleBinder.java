package com.test.retainedEvents;

import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;

public class ActivityWithRetainedAndField$LifeCycleBinder {
    public static void bind(LifeCycleAwareCollector<? extends ActivityWithRetainedAndField> collector, final ActivityWithRetainedAndField view) {
        view.myObject = collector.addRetainedFactory("myObjectProvider", view.myObjectProvider, false);
        MyObjectWithEvents2$LifeCycleBinder.bind(collector, view.myObject);
    }
}
