/*
 *   Copyright 2016 Fabio Collini.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package it.codingjam.lifecyclebinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class LifeCycleBinderFragment<T> extends Fragment implements LifeCycleAwareCollector<T> {

    public static final String LIFE_CYCLE_BINDER_FRAGMENT = "_LIFE_CYCLE_BINDER_FRAGMENT_";
    private static final String OBJECT_BINDER_CLASS = "objectBinderClass";
    private static final int LOADER_ID = 123;

    private T viewParam;

    private Map<String, ViewLifeCycleAware<?>> retainedObjects;

    private final List<ViewLifeCycleAware<? super T>> listeners = new ArrayList<>();

    public static <T> LifeCycleBinderFragment<T> get(FragmentManager fragmentManager) {
        return (LifeCycleBinderFragment<T>) fragmentManager.findFragmentByTag(LIFE_CYCLE_BINDER_FRAGMENT);
    }

    @NonNull
    public static <T> LifeCycleBinderFragment<T> create(Class<ObjectBinder<T, T>> objectBinderClass) {
        LifeCycleBinderFragment<T> fragment = new LifeCycleBinderFragment<>();
        Bundle args = new Bundle();
        args.putSerializable(OBJECT_BINDER_CLASS, objectBinderClass);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRetainedObjects();

        viewParam = (T) getParentFragment();
        if (viewParam == null) {
            viewParam = (T) getActivity();
        }

        invokeBinder();

        for (ViewLifeCycleAware<? super T> listener : listeners) {
            listener.onCreate(viewParam, savedInstanceState);
        }
    }

    private void invokeBinder() {
        Class<ObjectBinder<T, T>> objectBinderClass = (Class<ObjectBinder<T, T>>) getArguments().getSerializable(OBJECT_BINDER_CLASS);
        try {
            ObjectBinder<T, T> objectBinder = objectBinderClass.newInstance();
            objectBinder.bind(this, viewParam);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Illegal access exception instantiating class " + objectBinderClass.getName(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error invoking binding", e);
        }
    }

    private void initRetainedObjects() {
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<Map<String, ViewLifeCycleAware<?>>>() {
            @Override
            public Loader<Map<String, ViewLifeCycleAware<?>>> onCreateLoader(int id, Bundle args) {
                return new RetainedObjectsLoader(getActivity());
            }

            @Override
            public void onLoadFinished(Loader<Map<String, ViewLifeCycleAware<?>>> loader, Map<String, ViewLifeCycleAware<?>> data) {
            }

            @Override
            public void onLoaderReset(Loader<Map<String, ViewLifeCycleAware<?>>> loader) {
            }
        });

        retainedObjects = ((RetainedObjectsLoader) (Loader) getLoaderManager().getLoader(LOADER_ID)).retainedObjects;
    }

    @Override
    public void onStart() {
        super.onStart();
        for (ViewLifeCycleAware<? super T> listener : listeners) {
            listener.onStart(viewParam);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean hasMenu = false;
        for (ViewLifeCycleAware<? super T> listener : listeners) {
            listener.onResume(viewParam);
            hasMenu = hasMenu || listener.hasOptionsMenu();
        }
        if (hasMenu) {
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        for (ViewLifeCycleAware<? super T> listener : listeners) {
            listener.onCreateOptionsMenu(menu, inflater);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        for (ViewLifeCycleAware<? super T> listener : listeners) {
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
        for (ViewLifeCycleAware<? super T> listener : listeners) {
            listener.onPause(viewParam);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        for (ViewLifeCycleAware<? super T> listener : listeners) {
            listener.onStop(viewParam);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for (ViewLifeCycleAware<? super T> listener : listeners) {
            listener.onSaveInstanceState(viewParam, outState);
        }
    }

    @Override
    public void onDestroy() {
        for (ViewLifeCycleAware<? super T> listener : listeners) {
            listener.onDestroy(viewParam);
        }
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (ViewLifeCycleAware<? super T> listener : listeners) {
            listener.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public <R extends ViewLifeCycleAware<? super T>> R addRetainedFactory(String key, Callable<R> factory) {
        R listener = (R) retainedObjects.get(key);
        if (listener == null) {
            try {
                listener = factory.call();
                retainedObjects.put(key, listener);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        addLifeCycleAware(listener);
        return listener;
    }

    @Override
    public void addLifeCycleAware(ViewLifeCycleAware<? super T> lifeCycleAware) {
        listeners.add(lifeCycleAware);
    }
}
