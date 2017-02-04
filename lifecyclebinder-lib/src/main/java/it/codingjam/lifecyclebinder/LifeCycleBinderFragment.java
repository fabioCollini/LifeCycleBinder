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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class LifeCycleBinderFragment<T> extends Fragment implements LifeCycleAwareCollector {

    private static final String LIFE_CYCLE_BINDER_FRAGMENT = "_LIFE_CYCLE_BINDER_FRAGMENT_";
    private static final String OBJECT_BINDER_CLASS = "objectBinderClass";
    private static final int LOADER_ID = 123;

    private T viewParam;

    private Map<String, Object> retainedObjects;

    private final List<LifeCycleAware<? super T>> listeners = new ArrayList<>();

    @Nullable private Bundle savedInstanceStateOnCreate;

    static <T> LifeCycleBinderFragment<T> get(FragmentManager fragmentManager) {
        return (LifeCycleBinderFragment<T>) fragmentManager.findFragmentByTag(LIFE_CYCLE_BINDER_FRAGMENT);
    }

    @NonNull
    private static <T> LifeCycleBinderFragment<T> create(Class<?> objectBinderClass) {
        LifeCycleBinderFragment<T> fragment = new LifeCycleBinderFragment<>();
        Bundle args = new Bundle();
        args.putSerializable(OBJECT_BINDER_CLASS, objectBinderClass);
        fragment.setArguments(args);
        return fragment;
    }

    private static <T> void add(FragmentManager fragmentManager, LifeCycleBinderFragment<T> fragment) {
        fragmentManager.beginTransaction().add(fragment, LIFE_CYCLE_BINDER_FRAGMENT).commitNow();
    }

    static <T> LifeCycleBinderFragment<T> createAndAdd(FragmentManager fragmentManager, Class<?> c) {
        LifeCycleBinderFragment<T> fragment = create(c);
        add(fragmentManager, fragment);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.savedInstanceStateOnCreate = savedInstanceState;
        super.onCreate(savedInstanceState);
        retainedObjects = initRetainedObjects();

        T parentFragment = (T) getParentFragment();
        if (parentFragment == null) {
            viewParam = (T) getActivity();
        } else {
            viewParam = parentFragment;
        }
    }

    void invokeOnCreate() {
        invokeBindMethod();
        Bundle arguments;
        if (getParentFragment() == null) {
            arguments = getActivity().getIntent().getExtras();
        } else {
            arguments = getParentFragment().getArguments();
        }
        for (LifeCycleAware<? super T> listener : listeners) {
            listener.onCreate(viewParam, savedInstanceStateOnCreate, getActivity().getIntent(), arguments);
        }
        savedInstanceStateOnCreate = null;
    }

    private void invokeBindMethod() {
        Class<?> objectBinderClass = (Class<?>) getArguments().getSerializable(OBJECT_BINDER_CLASS);
        ReflectionUtils.invokeBindMethod(objectBinderClass, this, viewParam);
    }

    private Map<String, Object> initRetainedObjects() {
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<Map<String, Object>>() {
            @Override
            public Loader<Map<String, Object>> onCreateLoader(int id, Bundle args) {
                return new RetainedObjectsLoader(getActivity());
            }

            @Override
            public void onLoadFinished(Loader<Map<String, Object>> loader, Map<String, Object> data) {
            }

            @Override
            public void onLoaderReset(Loader<Map<String, Object>> loader) {
            }
        });

        return ((RetainedObjectsLoader) (Loader) getLoaderManager().getLoader(LOADER_ID)).retainedObjects;
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ret = super.onCreateView(inflater, container, savedInstanceState);
        for (LifeCycleAware<? super T> listener : listeners) {
            listener.onViewCreated(viewParam, savedInstanceState);
        }
        return ret;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        for (LifeCycleAware<? super T> listener : listeners) {
            listener.onDestroyView(viewParam);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        for (LifeCycleAware<? super T> listener : listeners) {
            listener.onStart(viewParam);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean hasMenu = false;
        for (LifeCycleAware<? super T> listener : listeners) {
            listener.onResume(viewParam);
            hasMenu = hasMenu || listener.hasOptionsMenu(viewParam);
        }
        if (hasMenu) {
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        for (LifeCycleAware<? super T> listener : listeners) {
            listener.onCreateOptionsMenu(viewParam, menu, inflater);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        for (LifeCycleAware<? super T> listener : listeners) {
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
        for (LifeCycleAware<? super T> listener : listeners) {
            listener.onPause(viewParam);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        for (LifeCycleAware<? super T> listener : listeners) {
            listener.onStop(viewParam);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for (LifeCycleAware<? super T> listener : listeners) {
            listener.onSaveInstanceState(viewParam, outState);
        }
    }

    @Override
    public void onDestroy() {
        boolean changingConfigurations = getActivity().isChangingConfigurations();
        for (LifeCycleAware<? super T> listener : listeners) {
            listener.onDestroy(viewParam, changingConfigurations);
        }
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (LifeCycleAware<? super T> listener : listeners) {
            listener.onActivityResult(viewParam, requestCode, resultCode, data);
        }
    }

    @Override
    public <R> R addRetainedFactory(String key, Callable<R> factory, boolean addInLifeCycleAwareList) {
        R listener = (R) retainedObjects.get(key);
        if (listener == null) {
            try {
                listener = factory.call();
                retainedObjects.put(key, listener);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (addInLifeCycleAwareList) {
            addLifeCycleAware((LifeCycleAware<? super T>) listener);
        }
        return listener;
    }

    @Override
    public void addLifeCycleAware(LifeCycleAware<?> lifeCycleAware) {
        listeners.add((LifeCycleAware<? super T>) lifeCycleAware);
    }

    @Override public <R> R getOrCreate(R lifeCycleAware, String key, Callable<R> factory) {
        R ret;
        if (lifeCycleAware != null) {
            ret = lifeCycleAware;
        } else {
            ret = addRetainedFactory(key, factory, false);
        }
        return ret;
    }
}
