package it.codingjam.lifecyclebinder;

import android.os.Bundle;
import android.view.MenuItem;

public interface ViewLifeCycleAware<T> {
    void onCreate(T view, Bundle bundle);

    void onStart(T view);

    void onResume(T view);

    boolean onOptionsItemSelected(T view, MenuItem item);

    void onPause(T view);

    void onStop(T view);

    void onSaveInstanceState(T view, Bundle bundle);

    void onDestroy(T view);
}
