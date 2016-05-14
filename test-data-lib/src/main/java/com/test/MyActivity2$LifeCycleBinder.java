package com.test;

import android.os.Bundle;

import it.codingjam.lifecyclebinder.ObjectBinder;

public class MyActivity2$LifeCycleBinder extends ObjectBinder<MyActivity2, MyActivity2> {
  private MyObjectWithParcelable$LifeCycleBinder myObject = new MyObjectWithParcelable$LifeCycleBinder("com.test.MyActivity2 myObject");

  public MyActivity2$LifeCycleBinder(String bundlePrefix) {
    super(bundlePrefix);
  }

  public void bind(final MyActivity2 view) {
    initRetainedObject(bundlePrefix + "myName", view.myObject);
    myObject.bind((MyObjectWithParcelable) retainedObjects.get(bundlePrefix + "myName"));
    listeners.addAll(myObject.getListeners());
  }
  public void saveInstanceState(MyActivity2 view, Bundle bundle) {
    myObject.saveInstanceState((MyObjectWithParcelable) retainedObjects.get(bundlePrefix + "myName"), bundle);
  }

  public void restoreInstanceState(MyActivity2 view, Bundle bundle) {
    myObject.restoreInstanceState((MyObjectWithParcelable) retainedObjects.get(bundlePrefix + "myName"), bundle);
  }
}
