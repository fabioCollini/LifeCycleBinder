package com.test;

import android.os.Bundle;

import it.codingjam.lifecyclebinder.ObjectBinder;

public final class MyActivity2$LifeCycleBinder extends ObjectBinder<MyActivity2, MyActivity2> {
  private MyObjectWithParcelable$LifeCycleBinder myObject = new MyObjectWithParcelable$LifeCycleBinder("com.test.MyActivity2 myObject");

  public MyActivity2$LifeCycleBinder() {
    super("com.test.MyActivity2");
  }

  public void bind(MyActivity2 view) {
    retainedObjectCallables.put(bundlePrefix + "myName", view.myObject);
    myObject.bind((MyObjectWithParcelable) retainedObjects.get(bundlePrefix + "myName"));
    listeners.addAll(myObject.getListeners());
    retainedObjectCallables.putAll(myObject.getRetainedObjectCallables());
  }
  public void saveInstanceState(MyActivity2 view, Bundle bundle) {
    myObject.saveInstanceState((MyObjectWithParcelable) retainedObjects.get(bundlePrefix + "myName"), bundle);
  }

  public void restoreInstanceState(MyActivity2 view, Bundle bundle) {
    myObject.restoreInstanceState((MyObjectWithParcelable) retainedObjects.get(bundlePrefix + "myName"), bundle);
  }
}
