package com.test;

import it.codingjam.lifecyclebinder.ObjectBinder;

public final class MyActivity$LifeCycleBinder extends ObjectBinder<MyActivity, MyActivity> {
    public void bind(MyActivity view) {
        listeners.add(view.myObject);
    }
}
