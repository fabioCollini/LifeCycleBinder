package com.test.nestedfactory;

import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;

public class ActivityMyObjectWithParcelableAndInnerObject$LifeCycleBinder {

    public static void bind(LifeCycleAwareCollector<? extends ActivityMyObjectWithParcelableAndInnerObject> collector, final ActivityMyObjectWithParcelableAndInnerObject view) {
        view.myObject = collector.addRetainedFactory("myObjectFactory", view.myObjectFactory, false);
        MyObjectWithParcelableAndInnerObject$LifeCycleBinder.bind(collector, view.myObject);
        collector.addLifeCycleAware(view.myObject);
        MyObjectWithParcelableAndInnerObject$LifeCycleBinder.bind(collector, collector.addRetainedFactory("myObjectFactoryNoField", view.myObjectFactoryNoField, true));
    }
}
