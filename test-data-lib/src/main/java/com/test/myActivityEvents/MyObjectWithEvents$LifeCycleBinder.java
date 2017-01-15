package com.test.myActivityEvents;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.test.MyView;
import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;

public class MyObjectWithEvents$LifeCycleBinder extends ObjectBinder<MyObjectWithEvents, MyView> {

    private MyObjectWithEvents delegate;

    public void bind(LifeCycleAwareCollector<? extends MyView> collector, final MyObjectWithEvents view) {
        this.delegate = view;
        collector.addLifeCycleAware(this);
    }

    public void onCreate(MyView view, Bundle arg0, Intent arg1, Bundle arg2) {
        delegate.myOnCreate(view, arg0, arg1, arg2);
    }

    public void onStart(MyView view) {
        delegate.myOnStart(view);
    }

    public void onResume(MyView view) {
        delegate.myOnResume(view);
    }

    public boolean hasOptionsMenu(MyView view) {
        return delegate.myHasOptionsMenu(view);
    }

    public void onCreateOptionsMenu(MyView view, Menu arg0, MenuInflater arg1) {
        delegate.myOnCreateOptionsMenu(view, arg0, arg1);
    }

    public boolean onOptionsItemSelected(MyView view, MenuItem arg0) {
        return delegate.myOnOptionsItemSelected(view, arg0);
    }

    public void onPause(MyView view) {
        delegate.myOnPause(view);
    }

    public void onStop(MyView view) {
        delegate.myOnStop(view);
    }

    public void onSaveInstanceState(MyView view, Bundle arg0) {
        delegate.myOnSaveInstanceState(view, arg0);
    }

    public void onDestroy(MyView view, boolean arg0) {
        delegate.myOnDestroy(view, arg0);
    }

    public void onActivityResult(MyView view, int arg0, int arg1, Intent arg2) {
        delegate.myOnActivityResult(view, arg0, arg1, arg2);
    }

    public void onViewCreated(MyView view, Bundle arg0) {
        delegate.myOnViewCreated(view, arg0);
    }

    public void onDestroyView(MyView view) {
        delegate.myOnDestroyView(view);
    }
}
