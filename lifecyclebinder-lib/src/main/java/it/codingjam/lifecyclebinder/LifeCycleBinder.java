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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class LifeCycleBinder {
    public static void bind(Fragment fragment) {
        bind(fragment, fragment.getChildFragmentManager());
    }

    public static void bind(FragmentActivity activity) {
        bind(activity, activity.getSupportFragmentManager());
    }

    public static <T extends Fragment> void bind(T fragment, Class<ObjectBinder<T, T>> objectBinderClass) {
        bind(fragment.getChildFragmentManager(), objectBinderClass);
    }

    public static <T extends FragmentActivity> void bind(T activity, Class<ObjectBinder<T, T>> objectBinderClass) {
        bind(activity.getSupportFragmentManager(), objectBinderClass);
    }

    private static <T> void bind(T obj, FragmentManager fragmentManager) {
        if (LifeCycleBinderFragment.get(fragmentManager) == null) {
            Class<ObjectBinder<T, T>> c = ReflectionUtils.getObjectBinderClass(obj);
            LifeCycleBinderFragment.createAndAdd(fragmentManager, c);
        }
    }

    private static <T> void bind(FragmentManager fragmentManager, Class<ObjectBinder<T, T>> objectBinderClass) {
        if (LifeCycleBinderFragment.get(fragmentManager) == null) {
            LifeCycleBinderFragment.createAndAdd(fragmentManager, objectBinderClass);
        }
    }

    public static void startActivityForResult(FragmentActivity activity, Intent intent, int requestCode) {
        LifeCycleBinderFragment.get(activity.getSupportFragmentManager()).startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(Fragment fragment, Intent intent, int requestCode) {
        LifeCycleBinderFragment.get(fragment.getFragmentManager()).startActivityForResult(intent, requestCode);
    }
}
