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

import android.util.Log;
import it.codingjam.lifecyclebinder.BindEvent;
import it.codingjam.lifecyclebinder.LifeCycleEvent;

import static it.codingjam.lifecyclebinder.LifeCycleEvent.ACTIVITY_RESULT;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.CREATE;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.CREATE_OPTION_MENU;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.DESTROY;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.HAS_OPTION_MENU;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.OPTION_ITEM_SELECTED;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.PAUSE;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.RESUME;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.SAVE_INSTANCE_STATE;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.START;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.STOP;

public class Logger {

    private static final String TAG = "ACTIVITY_LOG";

    @BindEvent({
            CREATE, START, RESUME, PAUSE, STOP, DESTROY, SAVE_INSTANCE_STATE, ACTIVITY_RESULT, HAS_OPTION_MENU, CREATE_OPTION_MENU,
            OPTION_ITEM_SELECTED
    })
    public void log(Object activity, LifeCycleEvent event) {
        Log.i(TAG, "Event " + event + ": " + activity);
    }
}
