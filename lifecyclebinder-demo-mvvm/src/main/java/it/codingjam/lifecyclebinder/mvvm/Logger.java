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

package it.codingjam.lifecyclebinder.mvvm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import it.codingjam.lifecyclebinder.BindEvent;

import static it.codingjam.lifecyclebinder.LifeCycleEvent.CREATE;

public class Logger {

    private static final String TAG = "ACTIVITY_LOG";

    @BindEvent(CREATE) public void onCreate(FragmentActivity activity, Bundle savedInstanceState, Intent intent, Bundle arguments) {
    }

    //@BindEvent({
    //        CREATE, START, RESUME, PAUSE, STOP, DESTROY, SAVE_INSTANCE_STATE, ACTIVITY_RESULT, HAS_OPTION_MENU, CREATE_OPTION_MENU,
    //        OPTION_ITEM_SELECTED
    //})
    //public void log(Object activity, LifeCycleEvent event) {
    //    Log.i(TAG, "Event " + event + ": " + activity);
    //}
}
