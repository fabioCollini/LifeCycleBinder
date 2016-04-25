package it.codingjam.lifecyclebinder;

import android.os.Bundle;
import android.view.MenuItem;

public class DefaultViewLifeCycleAware<T> implements ViewLifeCycleAware<T> {
    @Override
    public void onCreate(T activity, Bundle bundle) {
    }

    @Override
    public void onStart(T activity) {
    }

    @Override
    public void onResume(T activity) {
    }

    @Override
    public boolean onOptionsItemSelected(T activity, MenuItem item) {
        return false;
    }

    @Override
    public void onPause(T activity) {
    }

    @Override
    public void onStop(T activity) {
    }

    @Override
    public void onSaveInstanceState(T activity, Bundle bundle) {
    }

    @Override
    public void onDestroy(T activity) {
    }
}
