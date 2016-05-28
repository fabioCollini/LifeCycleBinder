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
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class LifeCycleBinder {
    public static void bind(Fragment fragment) {
        bind(fragment, "");
    }

    public static void bind(FragmentActivity activity) {
        bind(activity, "");
    }

    public static void bind(Fragment fragment, String key) {
        bind(key, fragment, fragment.getChildFragmentManager());
    }

    public static void bind(FragmentActivity activity, String key) {
        bind(key, activity, activity.getSupportFragmentManager());
    }

    private static <T> void bind(String key, T obj, FragmentManager fragmentManager) {
        if (LifeCycleBinderFragment.get(fragmentManager) == null) {
            Class<ObjectBinder<T, T>> c = getObjectBinderClass(obj);
            String bundlePrefix = obj.getClass().getName() + (key != null && !key.isEmpty() ? ObjectBinder.SEPARATOR + key : "");
            LifeCycleBinderFragment<T> fragment = new LifeCycleBinderFragment<>();
            fragment.init(c, bundlePrefix);
            fragmentManager.beginTransaction().add(fragment, LifeCycleBinderFragment.LIFE_CYCLE_BINDER_FRAGMENT).commitNow();
        }
    }

    @NonNull
    private static <T> Class<ObjectBinder<T, T>> getObjectBinderClass(T obj) {
        String className = obj.getClass().getName() + "$LifeCycleBinder";
        try {
            return (Class<ObjectBinder<T, T>>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error searching class " + className, e);
        }
    }

    public static void startActivityForResult(FragmentActivity activity, Intent intent, int requestCode) {
        LifeCycleBinderFragment.getOrCreate(activity.getSupportFragmentManager()).startActivityForResult(intent, requestCode);
    }
}
