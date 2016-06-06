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

package it.codingjam.lifecyclebinder.demo;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import it.codingjam.lifecyclebinder.DefaultLifeCycleAware;


public class FragmentLogger extends DefaultLifeCycleAware<Fragment> {

    public static final String TAG = "ACTIVITY_LOG";

    private String name;

    private static int sequence = 1;

    public FragmentLogger(String name) {
        this.name = (sequence++) + "_" + name;
    }

    @Override
    public void onCreate(Fragment activity, Bundle savedInstanceState, Intent intent, Bundle arguments) {
        Log.i(TAG, name + " Creating activity:" + activity);
    }

    @Override
    public void onStart(Fragment activity) {
//        Log.i(TAG, name + " Starting activity:" + activity);
    }

    @Override
    public void onResume(Fragment activity) {
        Log.i(TAG, name + " Resuming activity:" + activity);
    }

    @Override
    public void onPause(Fragment activity) {
        Log.i(TAG, name + " Pausing activity:" + activity);
    }

    @Override
    public void onStop(Fragment activity) {
//        Log.i(TAG, name + " Stopping activity:" + activity);
    }

    @Override
    public void onDestroy(Fragment activity, boolean changingConfigurations) {
        Log.i(TAG, name + " Destroying activity:" + activity);
    }
}
