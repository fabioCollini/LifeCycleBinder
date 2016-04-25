package it.codingjam.lifecyclebinder;

import android.os.Bundle;
import android.view.MenuItem;

public class DefaultViewLifeCycleAware<T> implements ViewLifeCycleAware<T> {
    @Override
    public void onCreate(T view, Bundle bundle) {
    }

    @Override
    public void onStart(T view) {
    }

    @Override
    public void onResume(T view) {
    }

    @Override
    public boolean onOptionsItemSelected(T view, MenuItem item) {
        return false;
    }

    @Override
    public void onPause(T view) {
    }

    @Override
    public void onStop(T view) {
    }

    @Override
    public void onSaveInstanceState(T view, Bundle bundle) {
    }

    @Override
    public void onDestroy(T view) {
    }
}
