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

package com.test.nestedfactory;

import com.test.MyView;

import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;
import it.codingjam.lifecyclebinder.ObjectBinder;

public class MyObjectWithParcelableAndInnerObject$LifeCycleBinder extends ObjectBinder<MyObjectWithParcelableAndInnerObject, MyView> {
    public void bind(LifeCycleAwareCollector<? extends MyView> collector, final MyObjectWithParcelableAndInnerObject view) {
        collector.addLifeCycleAware(view.myObject);
        view.myObject2 = collector.addRetainedFactory("myObject2Provider", view.myObject2Provider, false);
        collector.addLifeCycleAware(view.myObject2);
    }
}
