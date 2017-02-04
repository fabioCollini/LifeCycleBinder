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

package com.test.nested;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.test.MyObject;
import com.test.MyView;
import it.codingjam.lifecyclebinder.BindLifeCycle;
import it.codingjam.lifecyclebinder.LifeCycleAware;
import it.codingjam.lifecyclebinder.RetainedObjectProvider;
import java.util.concurrent.Callable;

class MyObjectWithInnerObject implements LifeCycleAware<Object> {

    @BindLifeCycle
    MyObject myObject;

    MyObject myObject2;

    @RetainedObjectProvider("myObject2")
    Callable<MyObject> myObject2Provider = new Callable<MyObject>() {
        @Override
        public MyObject call() throws Exception {
            return new MyObject();
        }
    };

    @Override
    public void onCreate(Object view, Bundle savedInstanceState, Intent intent, Bundle arguments) {

    }

    @Override
    public void onStart(Object view) {

    }

    @Override
    public void onResume(Object view) {

    }

    @Override
    public boolean hasOptionsMenu(Object view) {
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Object view, Menu menu, MenuInflater inflater) {

    }

    @Override
    public boolean onOptionsItemSelected(Object view, MenuItem item) {
        return false;
    }

    @Override
    public void onPause(Object view) {

    }

    @Override
    public void onStop(Object view) {

    }

    @Override
    public void onSaveInstanceState(Object view, Bundle bundle) {

    }

    @Override
    public void onDestroy(Object view, boolean changingConfigurations) {

    }

    @Override
    public void onActivityResult(Object view, int requestCode, int resultCode, Intent data) {

    }

    @Override public void onViewCreated(Object view, Bundle savedInstanceState) {

    }

    @Override public void onDestroyView(Object view) {

    }
}

public class ActivityMyObjectWithInnerObject extends FragmentActivity implements MyView {
    @BindLifeCycle
    MyObjectWithInnerObject myObject;
}
