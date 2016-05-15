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

package com.test.instanceState;

import android.os.Bundle;

import it.codingjam.lifecyclebinder.ObjectBinder;

public class MyActivity3$LifeCycleBinder extends ObjectBinder<MyActivity3, MyActivity3> {
    public MyActivity3$LifeCycleBinder(String bundlePrefix) {
        super(bundlePrefix);
    }

    public void bind(final MyActivity3 view) {
    }

    public void saveInstanceState(MyActivity3 view, Bundle bundle) {
        bundle.putParcelable(bundlePrefix + "myParcelable", view.myParcelable);
    }

    public void restoreInstanceState(MyActivity3 view, Bundle bundle) {
        view.myParcelable = bundle.getParcelable(bundlePrefix + "myParcelable");
    }
}
