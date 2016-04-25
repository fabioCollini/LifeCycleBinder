package it.codingjam.lifecyclebinder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class LifeCycleBinderFragment<T> extends Fragment {

    public static final String LIFE_CYCLE_BINDER_FRAGMENT = "_LIFE_CYCLE_BINDER_FRAGMENT_";

    private List<ViewLifeCycleAware<T>> listeners = new ArrayList<>();
    private T viewParam;

    @NonNull
    static <T> LifeCycleBinderFragment<T> getOrCreate(FragmentManager fragmentManager) {
        LifeCycleBinderFragment<T> fragment = (LifeCycleBinderFragment<T>) fragmentManager.findFragmentByTag(LIFE_CYCLE_BINDER_FRAGMENT);

        if (fragment == null) {
            fragment = new LifeCycleBinderFragment<>();
            fragmentManager.beginTransaction().add(fragment, LIFE_CYCLE_BINDER_FRAGMENT).commit();
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (listeners != null) {
            for (ViewLifeCycleAware<T> listener : listeners) {
                listener.onCreate(viewParam, savedInstanceState);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        for (ViewLifeCycleAware<T> listener : listeners) {
            listener.onStart(viewParam);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        for (ViewLifeCycleAware<T> listener : listeners) {
            listener.onResume(viewParam);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        for (ViewLifeCycleAware<T> listener : listeners) {
            boolean ret = listener.onOptionsItemSelected(viewParam, item);
            if (ret) {
                return ret;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        for (ViewLifeCycleAware<T> listener : listeners) {
            listener.onPause(viewParam);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        for (ViewLifeCycleAware<T> listener : listeners) {
            listener.onStop(viewParam);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for (ViewLifeCycleAware<T> listener : listeners) {
            listener.onSaveInstanceState(viewParam, outState);
        }
    }

    @Override
    public void onDestroy() {
        for (ViewLifeCycleAware<T> listener : listeners) {
            listener.onDestroy(viewParam);
        }
        super.onDestroy();
    }

    public void initListeners(T viewParam, List<ViewLifeCycleAware<T>> listeners) {
        this.viewParam = viewParam;
        this.listeners.addAll(listeners);
    }
}
