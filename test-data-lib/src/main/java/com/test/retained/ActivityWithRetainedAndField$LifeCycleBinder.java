package com.test.retained;

import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;
import it.codingjam.lifecyclebinder.ObjectBinder;

public class ActivityWithRetainedAndField$LifeCycleBinder extends ObjectBinder<ActivityWithRetainedAndField, ActivityWithRetainedAndField> {
    public void bind(LifeCycleAwareCollector<? extends ActivityWithRetainedAndField> collector, final ActivityWithRetainedAndField view) {
        view.myObject = collector.addRetainedFactory("myObjectProvider", view.myObjectProvider);
    }
}
