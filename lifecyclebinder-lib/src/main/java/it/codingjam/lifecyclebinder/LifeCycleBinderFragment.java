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

    private T viewParam;

    private ObjectBinder<T> objectBinder;

    private boolean valid;

    @NonNull
    static <T> LifeCycleBinderFragment<T> getOrCreate(FragmentManager fragmentManager) {
        LifeCycleBinderFragment<T> fragment = (LifeCycleBinderFragment<T>) fragmentManager.findFragmentByTag(LIFE_CYCLE_BINDER_FRAGMENT);

        if (fragment != null && !fragment.valid) {
            fragmentManager.beginTransaction().remove(fragment).commit();
            fragment = null;
        }

        if (fragment == null) {
            fragment = new LifeCycleBinderFragment<>();
            fragment.valid = true;
            fragmentManager.beginTransaction().add(fragment, LIFE_CYCLE_BINDER_FRAGMENT).commit();
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (objectBinder != null) {
            for (ViewLifeCycleAware<? super T> listener : objectBinder.getListeners()) {
                listener.onCreate(viewParam, savedInstanceState);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (objectBinder != null) {
            for (ViewLifeCycleAware<? super T> listener : objectBinder.getListeners()) {
                listener.onStart(viewParam);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (objectBinder != null) {
            boolean hasMenu = false;
            for (ViewLifeCycleAware<? super T> listener : objectBinder.getListeners()) {
                listener.onResume(viewParam);
                hasMenu = hasMenu || listener.hasOptionsMenu();
            }
            if (hasMenu) {
                setHasOptionsMenu(true);
            }
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
        if (objectBinder != null) {
            for (ViewLifeCycleAware<? super T> listener : objectBinder.getListeners()) {
                listener.onPause(viewParam);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (objectBinder != null) {
            for (ViewLifeCycleAware<? super T> listener : objectBinder.getListeners()) {
                listener.onStop(viewParam);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for (ViewLifeCycleAware<? super T> listener : objectBinder.getListeners()) {
            listener.onSaveInstanceState(viewParam, outState);
        }
    }

    @Override
    public void onDestroy() {
        if (objectBinder != null) {
            for (ViewLifeCycleAware<? super T> listener : objectBinder.getListeners()) {
                listener.onDestroy(viewParam);
            }
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

    public void init(T viewParam, ObjectBinder<T> objectBinder) {
        this.viewParam = viewParam;
        this.objectBinder = objectBinder;
    }
}
