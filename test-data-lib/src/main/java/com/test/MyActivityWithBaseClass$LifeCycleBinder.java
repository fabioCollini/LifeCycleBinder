package com.test;

import android.os.Bundle;

import it.codingjam.lifecyclebinder.ObjectBinder;

public class MyActivityWithBaseClass$LifeCycleBinder extends ObjectBinder<MyActivityWithBaseClass, MyActivityWithBaseClass> {

    private BaseClass$LifeCycleBinder superClass$lifeCycleBinder;

    public MyActivityWithBaseClass$LifeCycleBinder(String bundlePrefix) {
        super(bundlePrefix);
        superClass$lifeCycleBinder = new BaseClass$LifeCycleBinder(bundlePrefix + SEPARATOR + "superClass$lifeCycleBinder");
    }

    public void bind(final MyActivityWithBaseClass view) {
        listeners.add(view.myObject);
        superClass$lifeCycleBinder.bind(view);
        listeners.addAll(superClass$lifeCycleBinder.getListeners());
    }

    public void saveInstanceState(MyActivityWithBaseClass view, Bundle bundle) {
        superClass$lifeCycleBinder.saveInstanceState(view, bundle);
    }

    public void restoreInstanceState(MyActivityWithBaseClass view, Bundle bundle) {
        superClass$lifeCycleBinder.restoreInstanceState(view, bundle);
    }
}
