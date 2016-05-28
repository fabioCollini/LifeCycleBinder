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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class ObjectBinder<T, V> {

    protected List<ViewLifeCycleAware<? super V>> listeners = new ArrayList<>();

    protected Map<String, ViewLifeCycleAware<? super V>> retainedObjects = new HashMap<>();

    private RetainedObjectsFactory retainedObjectsFactory;

    public abstract void bind(T view);

    public List<ViewLifeCycleAware<? super V>> getListeners() {
        return listeners;
    }

    public void addListener(String key, ViewLifeCycleAware<? super V> listener) {
        listeners.add(listener);
        retainedObjects.put(key, listener);
    }

    public void setRetainedObjectsFactory(RetainedObjectsFactory retainedObjectsFactory) {
        this.retainedObjectsFactory = retainedObjectsFactory;
    }

    protected <O extends ViewLifeCycleAware<? super V>> O initRetainedObject(String key, Callable<? extends ViewLifeCycleAware<? super V>> factory) {
        ViewLifeCycleAware<? super V> retainedObject = retainedObjectsFactory.init(key, factory);
        addListener(key, retainedObject);
        return (O) retainedObject;
    }
}
