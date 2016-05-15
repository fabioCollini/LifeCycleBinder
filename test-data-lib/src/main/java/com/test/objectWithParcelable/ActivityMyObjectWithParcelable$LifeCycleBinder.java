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

package com.test.objectWithParcelable;

import android.os.Bundle;

import it.codingjam.lifecyclebinder.ObjectBinder;

public class ActivityMyObjectWithParcelable$LifeCycleBinder extends ObjectBinder<ActivityMyObjectWithParcelable, ActivityMyObjectWithParcelable> {
    private MyObjectWithParcelable2$LifeCycleBinder myObject;

    public ActivityMyObjectWithParcelable$LifeCycleBinder(String bundlePrefix) {
        super(bundlePrefix);
        myObject = new MyObjectWithParcelable2$LifeCycleBinder(bundlePrefix + SEPARATOR + "myObject");
    }

    public void bind(final ActivityMyObjectWithParcelable view) {
        listeners.add(view.myObject);
        myObject.bind(view.myObject);
        listeners.addAll(myObject.getListeners());
    }

    public void saveInstanceState(ActivityMyObjectWithParcelable view, Bundle bundle) {
        myObject.saveInstanceState(view.myObject, bundle);
    }

    public void restoreInstanceState(ActivityMyObjectWithParcelable view, Bundle bundle) {
        myObject.restoreInstanceState(view.myObject, bundle);
    }
}
