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

package it.codingjam.lifecyclebinder.mvp;


import android.os.Bundle;
import android.util.Log;

import it.codingjam.lifecyclebinder.DefaultViewLifeCycleAware;


public class Logger extends DefaultViewLifeCycleAware<Object> {

    private static final String TAG = "ACTIVITY_LOG";

    @Override
    public void onCreate(Object activity, Bundle bundle) {
        Log.i(TAG, "Creating:" + activity);
    }

    @Override
    public void onStart(Object activity) {
        Log.i(TAG, "Starting:" + activity);
    }

    @Override
    public void onResume(Object activity) {
        Log.i(TAG, "Resuming:" + activity);
    }

    @Override
    public void onPause(Object activity) {
        Log.i(TAG, "Pausing:" + activity);
    }

    @Override
    public void onStop(Object activity) {
        Log.i(TAG, "Stopping:" + activity);
    }

    @Override
    public void onDestroy(Object activity) {
        Log.i(TAG, "Destroying:" + activity);
    }
}
