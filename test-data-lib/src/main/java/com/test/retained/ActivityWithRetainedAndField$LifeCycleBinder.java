package com.test.retained;

import com.test.MyObject$LifeCycleBinder;
import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;

public class ActivityWithRetainedAndField$LifeCycleBinder {
    public static void bind(LifeCycleAwareCollector collector, final ActivityWithRetainedAndField view) {
        view.myObject = MyObject$LifeCycleBinder.bind(collector, collector.getOrCreate(null, "myObjectProvider", view.myObjectProvider), true);
    }
}
