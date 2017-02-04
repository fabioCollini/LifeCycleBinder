package com.test.myActivity;

import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;
import java.util.concurrent.Callable;

public class MyObject$LifeCycleBinder {

    public static MyObject bind(LifeCycleAwareCollector collector, MyObject lifeCycleAware, String key, Callable<MyObject> factory, boolean addInList) {
        MyObject ret = collector.getOrCreate(lifeCycleAware, key, factory);
        if (addInList) {
            collector.addLifeCycleAware(ret);
        }
        return ret;
    }
}
