package com.test.myObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.test.MyView;
import it.codingjam.lifecyclebinder.BindLifeCycle;
import it.codingjam.lifecyclebinder.LifeCycleAware;

@BindLifeCycle
class MyObject implements LifeCycleAware<MyView> {
    @Override
    public void onCreate(MyView view, Bundle savedInstanceState, Intent intent, Bundle arguments) {

    }

    @Override
    public void onStart(MyView view) {

    }

    @Override
    public void onResume(MyView view) {

    }

    @Override
    public boolean hasOptionsMenu(MyView view) {
        return false;
    }

    @Override
    public void onCreateOptionsMenu(MyView view, Menu menu, MenuInflater inflater) {

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
    public void onDestroy(MyView view, boolean changingConfigurations) {

    }

    @Override
    public void onActivityResult(MyView view, int requestCode, int resultCode, Intent data) {

    }

    @Override public void onViewCreated(MyView view, Bundle savedInstanceState) {

    }

    @Override public void onDestroyView(MyView view) {

    }
}
