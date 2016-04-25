package com.test;

import android.os.Bundle;
import android.view.MenuItem;

import it.codingjam.lifecyclebinder.ViewLifeCycleAware;

public class MyObject implements ViewLifeCycleAware<MyView> {
    @Override
    public void onCreate(MyView view, Bundle bundle) {

    }

    @Override
    public void onStart(MyView view) {

    }

    @Override
    public void onResume(MyView view) {

    }

    @Override
    public boolean onOptionsItemSelected(MyView view, MenuItem item) {
        return false;
    }

    @Override
    public void onPause(MyView view) {

    }

    @Override
    public void onStop(MyView view) {

    }

    @Override
    public void onSaveInstanceState(MyView view, Bundle bundle) {

    }

    @Override
    public void onDestroy(MyView view) {

    }
}
