package com.test.nestedfactory;

import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;

public class ActivityMyObjectWithParcelableAndInnerObject$LifeCycleBinder {

    public static void bind(LifeCycleAwareCollector collector, final ActivityMyObjectWithParcelableAndInnerObject view) {
        view.myObject = MyObjectWithParcelableAndInnerObject$LifeCycleBinder.bind(collector, collector.getOrCreate(null, "myObjectFactory", view.myObjectFactory), true);
        MyObjectWithParcelableAndInnerObject$LifeCycleBinder.bind(collector, collector.getOrCreate(null, "myObjectFactoryNoField", view.myObjectFactoryNoField), true);
    }
}
