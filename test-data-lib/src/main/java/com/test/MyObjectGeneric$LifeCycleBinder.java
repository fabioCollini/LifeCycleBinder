package com.test;

import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;

public class MyObjectGeneric$LifeCycleBinder {

    public static MyObjectGeneric bind(LifeCycleAwareCollector collector, MyObjectGeneric lifeCycleAware, boolean addInList) {
        if (addInList) {
            collector.addLifeCycleAware(lifeCycleAware);
        }
        return lifeCycleAware;
    }
}
