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

package com.test.myActivityEventsNoViewParam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.test.MyView;
import it.codingjam.lifecyclebinder.BindEvent;
import it.codingjam.lifecyclebinder.BindLifeCycle;

import static it.codingjam.lifecyclebinder.LifeCycleEvent.ACTIVITY_RESULT;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.CREATE;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.CREATE_OPTION_MENU;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.DESTROY;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.DESTROY_VIEW;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.HAS_OPTION_MENU;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.OPTION_ITEM_SELECTED;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.PAUSE;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.RESUME;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.SAVE_INSTANCE_STATE;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.START;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.STOP;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.VIEW_CREATED;

public class MyActivity extends FragmentActivity implements MyView {
    @BindLifeCycle
    MyObjectWithEvents myObject;
}

class MyObjectWithEvents {
    @BindEvent(CREATE) public void myOnCreate(Bundle savedInstanceState, Intent intent, Bundle arguments) {
    }

    @BindEvent(START) public void myOnStart() {
    }

    @BindEvent(RESUME) public void myOnResume() {
    }

    @BindEvent(HAS_OPTION_MENU) public boolean myHasOptionsMenu() {
        return false;
    }

    @BindEvent(CREATE_OPTION_MENU) public void myOnCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @BindEvent(OPTION_ITEM_SELECTED) public boolean myOnOptionsItemSelected(MenuItem item) {
        return false;
    }

    @BindEvent(PAUSE) public void myOnPause() {
    }

    @BindEvent(STOP) public void myOnStop() {
    }

    @BindEvent(SAVE_INSTANCE_STATE) public void myOnSaveInstanceState(Bundle bundle) {
    }

    @BindEvent(DESTROY) public void myOnDestroy(boolean changingConfigurations) {
    }

    @BindEvent(ACTIVITY_RESULT) public void myOnActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @BindEvent(VIEW_CREATED) public void myOnViewCreated(Bundle savedInstanceState) {
    }

    @BindEvent(DESTROY_VIEW) public void myOnDestroyView() {
    }
}

