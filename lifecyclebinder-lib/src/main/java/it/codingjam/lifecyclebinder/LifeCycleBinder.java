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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class LifeCycleBinder {
    public static void bind(Bundle savedInstanceState, Fragment fragment) {
        bind(savedInstanceState, fragment, "");
    }

    public static void bind(Bundle savedInstanceState, FragmentActivity activity) {
        bind(savedInstanceState, activity, "");
    }

    public static void bind(Bundle savedInstanceState, Fragment fragment, String key) {
        if (savedInstanceState == null) {
            bind(key, fragment, fragment.getChildFragmentManager(), fragment.getActivity().getSupportFragmentManager());
        }
    }

    public static void bind(Bundle savedInstanceState, FragmentActivity activity, String key) {
        if (savedInstanceState == null) {
            bind(key, activity, activity.getSupportFragmentManager(), activity.getSupportFragmentManager());
        }
    }

    private static <T> void bind(String key, T obj, FragmentManager fragmentManager, FragmentManager activityFragmentManager) {
        LifeCycleRetainedFragment retainedFragment = LifeCycleRetainedFragment.getOrCreateRetainedFragment(activityFragmentManager);
        LifeCycleBinderFragment<T> fragment = LifeCycleBinderFragment.getOrCreate(fragmentManager);
        Class<ObjectBinder<T, T>> c = getObjectBinderClass(obj);
        fragment.init(c, obj.getClass().getName() + (key != null && !key.isEmpty() ? ObjectBinder.SEPARATOR + key : ""));
        fragmentManager.executePendingTransactions();
        System.out.println(fragment);
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

    public static <T> T getRetainedObject(FragmentActivity activity, String name) {
        return getRetainedObject(activity.getSupportFragmentManager(), activity.getSupportFragmentManager(), name);
    }

    public static <T> T getRetainedObject(Fragment fragment, String name) {
        return getRetainedObject(fragment.getChildFragmentManager(), fragment.getActivity().getSupportFragmentManager(), name);
    }

    private static <T> T getRetainedObject(FragmentManager fragmentManager, FragmentManager activityFragmentManager, String name) {
        LifeCycleBinderFragment<T> fragment = LifeCycleBinderFragment.getOrCreate(fragmentManager);
        return (T) LifeCycleRetainedFragment.getOrCreateRetainedFragment(activityFragmentManager).map.get(fragment.getBundlePrefix() + name);
    }

    public static void startActivityForResult(FragmentActivity activity, Intent intent, int requestCode) {
        LifeCycleBinderFragment.getOrCreate(activity.getSupportFragmentManager()).startActivityForResult(intent, requestCode);
    }
}
