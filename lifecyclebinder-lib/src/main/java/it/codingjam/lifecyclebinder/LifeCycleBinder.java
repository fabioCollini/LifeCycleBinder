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

import java.util.Map;

public class LifeCycleBinder {
    public static void bind(Fragment fragment) {
        bind(fragment, fragment.getChildFragmentManager(), fragment.getActivity().getSupportFragmentManager());
    }

    public static void bind(FragmentActivity activity) {
        bind(activity, activity.getSupportFragmentManager(), activity.getSupportFragmentManager());
    }

    private static <T> void bind(T obj, FragmentManager fragmentManager, FragmentManager activityFragmentManager) {
        Map<String, ViewLifeCycleAware> retainedObjects = (Map) LifeCycleRetainedFragment.getOrCreateRetainedFragment(activityFragmentManager).map;
        LifeCycleBinderFragment<T> fragment = LifeCycleBinderFragment.getOrCreate(fragmentManager);
        fragment.init(obj);
        String className = obj.getClass().getName() + "$LifeCycleBinder";
        try {
            Class<?> c = Class.forName(className);
            ObjectBinder<T> objectBinder = (ObjectBinder<T>) c.newInstance();
            if (objectBinder.containsRetainedObjects()) {
            }
            objectBinder.bind(obj, fragment, retainedObjects);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error searching class " + className, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Error instantiating class " + className, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error instantiating class " + className, e);
        } catch (Exception e) {
            throw new RuntimeException("Error invoking binding", e);
        }
    }

    public static <T> T getRetainedObject(FragmentActivity activity, String name) {
        return getRetainedObject(activity.getSupportFragmentManager(), name);
    }

    public static <T> T getRetainedObject(Fragment fragment, String name) {
        return getRetainedObject(fragment.getFragmentManager(), name);
    }

    private static <T> T getRetainedObject(FragmentManager fragmentManager, String name) {
        return (T) LifeCycleRetainedFragment.getOrCreateRetainedFragment(fragmentManager).map.get(name);
    }

    public static void startActivityForResult(FragmentActivity activity, Intent intent, int requestCode) {
        LifeCycleBinderFragment.getOrCreate(activity.getSupportFragmentManager()).startActivityForResult(intent, requestCode);
    }
}
