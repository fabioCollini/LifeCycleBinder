package com.test.myActivityEvents;

import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;

public class MyActivity$LifeCycleBinder extends ObjectBinder<MyActivity, MyActivity> {
    private MyObjectWithEvents$LifeCycleBinder myObject = new MyObjectWithEvents$LifeCycleBinder();

    public void bind(LifeCycleAwareCollector<? extends MyActivity> collector, final MyActivity view) {
        myObject.bind(collector, view.myObject);
    }
}
