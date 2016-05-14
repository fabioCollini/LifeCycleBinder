package com.test;

import it.codingjam.lifecyclebinder.ObjectBinder;

public class MyActivity$LifeCycleBinder extends ObjectBinder<MyActivity, MyActivity> {
    public MyActivity$LifeCycleBinder(String bundlePrefix) {
        super(bundlePrefix);
    }

    public void bind(final MyActivity view) {
        listeners.add(view.myObject);
    }
}
