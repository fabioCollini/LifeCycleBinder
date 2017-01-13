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

package com.test.retainedObjectsInBaseClassWithProvider;

import com.test.MyObject;
import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;
import it.codingjam.lifecyclebinder.ObjectBinder;
import java.util.concurrent.Callable;

public class ActivityWithRetainedProvider$LifeCycleBinder extends ObjectBinder<ActivityWithRetainedProvider, ActivityWithRetainedProvider> {
    public void bind(LifeCycleAwareCollector<? extends ActivityWithRetainedProvider> collector, final ActivityWithRetainedProvider view) {
        view.myObjectInBaseActivity = collector.addRetainedFactory("myObject", new Callable<MyObject>() {
            @Override
            public MyObject call() throws Exception {
                return view.myObject.get();
            }
        }, false);
        collector.addLifeCycleAware(view.myObjectInBaseActivity);
    }
}