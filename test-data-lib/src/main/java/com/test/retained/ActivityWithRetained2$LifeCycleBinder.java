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

package com.test.retained;

import com.test.MyObject$LifeCycleBinder;
import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;

public class ActivityWithRetained2$LifeCycleBinder {
    public static void bind(LifeCycleAwareCollector collector, final ActivityWithRetained2 view) {
        MyObject$LifeCycleBinder.bind(collector, collector.getOrCreate(null, "myObject", view.myObject), true);
        MyObject$LifeCycleBinder.bind(collector, collector.getOrCreate(null, "myObject2", view.myObject2), true);
    }
}
