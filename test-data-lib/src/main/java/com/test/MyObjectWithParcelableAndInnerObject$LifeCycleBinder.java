package com.test;

import android.os.Bundle;

import it.codingjam.lifecyclebinder.ObjectBinder;

public class MyObjectWithParcelableAndInnerObject$LifeCycleBinder extends ObjectBinder<MyObjectWithParcelableAndInnerObject, MyView> {

    public MyObjectWithParcelableAndInnerObject$LifeCycleBinder(String bundlePrefix) {
        super(bundlePrefix);
    }

    public void bind(final MyObjectWithParcelableAndInnerObject view) {
        listeners.add(view.myObject);
    }

    public void saveInstanceState(MyObjectWithParcelableAndInnerObject view, Bundle bundle) {
        bundle.putParcelable(bundlePrefix + "myParcelable", view.myParcelable);
    }

    public void restoreInstanceState(MyObjectWithParcelableAndInnerObject view, Bundle bundle) {
        view.myParcelable = bundle.getParcelable(bundlePrefix + "myParcelable");
    }
}
