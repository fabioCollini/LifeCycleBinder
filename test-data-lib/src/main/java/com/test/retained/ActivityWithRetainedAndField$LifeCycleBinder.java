package com.test.retained;

import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;

public class ActivityWithRetainedAndField$LifeCycleBinder {
    public static void bind(LifeCycleAwareCollector collector, final ActivityWithRetainedAndField view) {
        view.myObject = collector.addRetainedFactory("myObjectProvider", view.myObjectProvider, false);
        collector.addLifeCycleAware(view.myObject);
    }
}
