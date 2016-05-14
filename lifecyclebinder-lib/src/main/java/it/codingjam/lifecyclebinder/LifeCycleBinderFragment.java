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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class LifeCycleBinderFragment<T> extends Fragment {

    public static final String LIFE_CYCLE_BINDER_FRAGMENT = "_LIFE_CYCLE_BINDER_FRAGMENT_";
    public static final String OBJECT_BINDER_CLASS = "objectBinderClass";

    private T viewParam;

    private ObjectBinder<T, T> objectBinder;

    private Class<ObjectBinder<T, T>> objectBinderClass;

    @NonNull
    static <T> LifeCycleBinderFragment<T> getOrCreate(FragmentManager fragmentManager) {
        LifeCycleBinderFragment<T> fragment = (LifeCycleBinderFragment<T>) fragmentManager.findFragmentByTag(LIFE_CYCLE_BINDER_FRAGMENT);

        if (fragment == null) {
            System.out.println("Logger: recreating LifeCycleBinderFragment");
            fragment = new LifeCycleBinderFragment<>();
            fragmentManager.beginTransaction().add(fragment, LIFE_CYCLE_BINDER_FRAGMENT).commit();
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            objectBinderClass = (Class<ObjectBinder<T, T>>) savedInstanceState.getSerializable(OBJECT_BINDER_CLASS);
        }
        viewParam = (T) getParentFragment();
        if (viewParam == null) {
            viewParam = (T) getActivity();
        }

        try {
            LifeCycleRetainedFragment retainedFragment = LifeCycleRetainedFragment.getOrCreateRetainedFragment(getActivity().getSupportFragmentManager());
            objectBinder = createObjectBinder();
            objectBinder.setRetainedObjectsFactory(retainedFragment);
            objectBinder.bind(viewParam);
        } catch (Exception e) {
            throw new RuntimeException("Error invoking binding", e);
        }

        if (savedInstanceState != null) {
            objectBinder.restoreInstanceState(viewParam, savedInstanceState);
        }
        for (ViewLifeCycleAware<? super T> listener : objectBinder.getListeners()) {
            listener.onCreate(viewParam, savedInstanceState);
        }
    }

    private ObjectBinder<T, T> createObjectBinder() {
        try {
            return objectBinderClass.newInstance();
        } catch (java.lang.InstantiationException e) {
            throw new RuntimeException("Error instantiating class " + objectBinderClass.getName(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Illegal access exception instantiating class " + objectBinderClass.getName(), e);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        for (ViewLifeCycleAware<? super T> listener : objectBinder.getListeners()) {
            listener.onStart(viewParam);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean hasMenu = false;
        for (ViewLifeCycleAware<? super T> listener : objectBinder.getListeners()) {
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
        for (ViewLifeCycleAware<? super T> listener : objectBinder.getListeners()) {
            listener.onCreateOptionsMenu(menu, inflater);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        for (ViewLifeCycleAware<? super T> listener : objectBinder.getListeners()) {
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
        for (ViewLifeCycleAware<? super T> listener : objectBinder.getListeners()) {
            listener.onPause(viewParam);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        for (ViewLifeCycleAware<? super T> listener : objectBinder.getListeners()) {
            listener.onStop(viewParam);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(OBJECT_BINDER_CLASS, objectBinderClass);
        objectBinder.saveInstanceState(viewParam, outState);
        for (ViewLifeCycleAware<? super T> listener : objectBinder.getListeners()) {
            listener.onSaveInstanceState(viewParam, outState);
        }
    }

    @Override
    public void onDestroy() {
        for (ViewLifeCycleAware<? super T> listener : objectBinder.getListeners()) {
            listener.onDestroy(viewParam);
        }
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (ViewLifeCycleAware<? super T> listener : objectBinder.getListeners()) {
            listener.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void init(Class<ObjectBinder<T, T>> objectBinderClass) {
        this.objectBinderClass = objectBinderClass;
    }
}
