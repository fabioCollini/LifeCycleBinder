package com.test;

import android.os.Bundle;

import it.codingjam.lifecyclebinder.ObjectBinder;

public final class MyActivity3$LifeCycleBinder extends ObjectBinder<MyActivity3, MyActivity3> {
    public MyActivity3$LifeCycleBinder() {
        super("com.test.MyActivity3");
    }

    public void bind(MyActivity3 view) {
    }

    public void saveInstanceState(MyActivity3 view, Bundle bundle) {
        bundle.putParcelable(bundlePrefix + "myParcelable", view.myParcelable);
    }

    public void restoreInstanceState(MyActivity3 view, Bundle bundle) {
        view.myParcelable = bundle.getParcelable(bundlePrefix + "myParcelable");
    }
}
