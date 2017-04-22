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

package it.codingjam.lifecyclebinder.testapp;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import it.codingjam.lifecyclebinder.BindLifeCycle;
import it.codingjam.lifecyclebinder.LifeCycleAware;

@BindLifeCycle
public class Logger implements LifeCycleAware<Object> {

    private String name;

    public List<String> logs;

    private static int sequence;

    public static List<String> ALL_LOGS = new ArrayList<>();

    public Logger(String name) {
        this.name = name + sequence++;
        logs = ALL_LOGS;
    }

    public static void reset() {
        sequence = 1;
        ALL_LOGS = new ArrayList<>();
    }

    @Override
    public void onCreate(Object view, Bundle savedInstanceState, Intent intent, Bundle arguments) {
        log(name + ": " + "onCreate");
    }

    private void log(String object) {
        Log.d("Logger", object);
        logs.add(object);
    }

    @Override
    public void onStart(Object view) {
        log(name + ": " + "onStart");
    }

    @Override
    public void onResume(Object view) {
        log(name + ": " + "onResume");
    }

    @Override
    public boolean hasOptionsMenu(Object view) {
        log(name + ": " + "hasOptionsMenu");
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Object view, Menu menu, MenuInflater inflater) {
        log(name + ": " + "onCreateOptionsMenu");
    }

    @Override
    public boolean onOptionsItemSelected(Object view, MenuItem item) {
        log(name + ": " + "onOptionsItemSelected");
        return false;
    }

    @Override
    public void onPause(Object view) {
        log(name + ": " + "onPause");
    }

    @Override
    public void onStop(Object view) {
        log(name + ": " + "onStop");
    }

    @Override
    public void onSaveInstanceState(Object view, Bundle bundle) {
        log(name + ": " + "onSaveInstanceState");
    }

    @Override
    public void onDestroy(Object view, boolean changingConfigurations) {
        log(name + ": " + "onDestroy");
    }

    @Override
    public void onActivityResult(Object view, int requestCode, int resultCode, Intent data) {
        log(name + ": " + "onActivityResult");
    }

    @Override public void onViewCreated(Object view, Bundle savedInstanceState) {
    }

    @Override public void onDestroyView(Object view) {
    }
}
