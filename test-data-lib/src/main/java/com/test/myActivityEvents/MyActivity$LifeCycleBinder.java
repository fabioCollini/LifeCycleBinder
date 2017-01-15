package com.test.myActivityEvents;

import it.codingjam.lifecyclebinder.LifeCycleAware;
import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;
import it.codingjam.lifecyclebinder.ObjectBinder;

public class MyActivity$LifeCycleBinder extends ObjectBinder<MyActivity, MyActivity> {
    private MyObjectWithEvents$LifeCycleBinder myObject = new MyObjectWithEvents$LifeCycleBinder();

    public void bind(LifeCycleAwareCollector<? extends MyActivity> collector, final MyActivity view) {
        if (view.myObject instanceof LifeCycleAware) {
            collector.addLifeCycleAware((LifeCycleAware) view.myObject);
        }
        myObject.bind(collector, view.myObject);
    }
}
