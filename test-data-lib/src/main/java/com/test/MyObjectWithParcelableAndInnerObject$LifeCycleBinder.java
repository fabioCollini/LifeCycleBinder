package com.test;

import android.os.Bundle;

import it.codingjam.lifecyclebinder.ObjectBinder;

public final class MyObjectWithParcelableAndInnerObject$LifeCycleBinder extends ObjectBinder<MyObjectWithParcelableAndInnerObject, MyView> {

    public void bind(MyObjectWithParcelableAndInnerObject view) {
        listeners.add(view.myObject);
    }

    public void saveInstanceState(MyObjectWithParcelableAndInnerObject view, Bundle bundle) {
        bundle.putParcelable("myParcelable", view.myParcelable);
    }

    public void restoreInstanceState(MyObjectWithParcelableAndInnerObject view, Bundle bundle) {
        view.myParcelable = bundle.getParcelable("myParcelable");
    }
}
