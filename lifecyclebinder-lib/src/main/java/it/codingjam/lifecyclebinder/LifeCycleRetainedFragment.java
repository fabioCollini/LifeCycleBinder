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

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class LifeCycleRetainedFragment extends Fragment {
    private static final String RETAINED_FRAGMENT = "_LIFE_CYCLE_RETAINED_FRAGMENT_";

    public final Map<String, ViewLifeCycleAware<?>> map = new HashMap<>();

    public LifeCycleRetainedFragment() {
        setRetainInstance(true);
    }

    @NonNull
    public static LifeCycleRetainedFragment getOrCreateRetainedFragment(FragmentManager fragmentManager) {
        LifeCycleRetainedFragment fragment = (LifeCycleRetainedFragment) fragmentManager.findFragmentByTag(RETAINED_FRAGMENT);

        if (fragment == null) {
            fragment = new LifeCycleRetainedFragment();
            fragmentManager.beginTransaction().add(fragment, RETAINED_FRAGMENT).commit();
            fragmentManager.executePendingTransactions();
        }
        return fragment;
    }

    public <T> void init(ObjectBinder<T> objectBinder) {
        Map<String, Callable<? extends ViewLifeCycleAware<? super T>>> retainedObjectCallables = objectBinder.getRetainedObjectCallables();
        for (Map.Entry<String, Callable<? extends ViewLifeCycleAware<? super T>>> entry : retainedObjectCallables.entrySet()) {
            String key = entry.getKey();
            ViewLifeCycleAware<? super T> listener = (ViewLifeCycleAware<? super T>) map.get(key);
            if (listener == null) {
                try {
                    listener = entry.getValue().call();
                    map.put(key, listener);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            objectBinder.addListener(key, listener);
        }
    }
}
