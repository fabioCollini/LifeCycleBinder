package com.test.retainedObjectsWithProvider;

import com.test.MyObject;
import com.test.MyObject$LifeCycleBinder;
import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;
import java.util.concurrent.Callable;

public class ActivityWithRetainedProvider$LifeCycleBinder {
    public static void bind(LifeCycleAwareCollector collector, final ActivityWithRetainedProvider view) {
        MyObject$LifeCycleBinder.bind(collector, null, "myObject", new Callable<MyObject>() {
            @Override
            public MyObject call() throws Exception {
                return view.myObject.get();
            }
        }, true);
    }
}
