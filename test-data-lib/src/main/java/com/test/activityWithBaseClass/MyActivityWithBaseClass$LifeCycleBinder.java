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

package com.test.activityWithBaseClass;

import android.os.Bundle;

import it.codingjam.lifecyclebinder.ObjectBinder;

public class MyActivityWithBaseClass$LifeCycleBinder extends ObjectBinder<MyActivityWithBaseClass, MyActivityWithBaseClass> {

    private BaseClass$LifeCycleBinder superClass$lifeCycleBinder;

    public MyActivityWithBaseClass$LifeCycleBinder(String bundlePrefix) {
        super(bundlePrefix);
        superClass$lifeCycleBinder = new BaseClass$LifeCycleBinder(bundlePrefix + SEPARATOR + "superClass$lifeCycleBinder");
    }

    public void bind(final MyActivityWithBaseClass view) {
        listeners.add(view.myObject);
        superClass$lifeCycleBinder.bind(view);
        listeners.addAll(superClass$lifeCycleBinder.getListeners());
    }

    public void saveInstanceState(MyActivityWithBaseClass view, Bundle bundle) {
        superClass$lifeCycleBinder.saveInstanceState(view, bundle);
    }

    public void restoreInstanceState(MyActivityWithBaseClass view, Bundle bundle) {
        superClass$lifeCycleBinder.restoreInstanceState(view, bundle);
    }
}
