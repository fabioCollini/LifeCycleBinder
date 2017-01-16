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

package com.test.retainedEvents;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.test.MyView;
import it.codingjam.lifecyclebinder.BindEvent;
import it.codingjam.lifecyclebinder.RetainedObjectProvider;
import java.util.concurrent.Callable;

import static it.codingjam.lifecyclebinder.LifeCycleEvent.CREATE;

public class ActivityWithRetainedAndField extends FragmentActivity implements MyView {
    @RetainedObjectProvider("myObject")
    Callable<MyObjectWithEvents2> myObjectProvider = new Callable<MyObjectWithEvents2>() {
        @Override
        public MyObjectWithEvents2 call() throws Exception {
            return new MyObjectWithEvents2();
        }
    };

    MyObjectWithEvents2 myObject;
}

class MyObjectWithEvents2 {
    @BindEvent(CREATE) public void myOnCreate(MyView view, Bundle savedInstanceState, Intent intent, Bundle arguments) {
    }
}
