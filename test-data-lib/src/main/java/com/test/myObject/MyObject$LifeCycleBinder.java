package com.test.myObject;

import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;

public class MyObject$LifeCycleBinder {

    public static MyObject bind(LifeCycleAwareCollector collector, MyObject lifeCycleAware, boolean addInList) {
        if (addInList) {
            collector.addLifeCycleAware(lifeCycleAware);
        }
        return lifeCycleAware;
    }
}
