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

class ReflectionUtils {
    static <T> void invokeBindMethod(Class<ObjectBinder<T, T>> objectBinderClass, LifeCycleBinderFragment<T> collector, T viewParam) {
        try {
            ObjectBinder<T, T> objectBinder = objectBinderClass.newInstance();
            objectBinder.bind(collector, viewParam);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Illegal access exception instantiating class " + objectBinderClass.getName(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error invoking binding", e);
        }
    }

    @NonNull
    static <T> Class<ObjectBinder<T, T>> getObjectBinderClass(T obj) {
        String className = obj.getClass().getName() + "$LifeCycleBinder";
        try {
            return (Class<ObjectBinder<T, T>>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error searching class " + className, e);
        }
    }
}
