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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import it.codingjam.lifecyclebinder.ViewLifeCycleAware;


public class Logger implements ViewLifeCycleAware<Object> {

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
    public void onCreate(Object view, Bundle bundle) {
        logs.add(name + ": " + "onCreate");
    }

    @Override
    public void onStart(Object view) {
        logs.add(name + ": " + "onStart");
    }

    @Override
    public void onResume(Object view) {
        logs.add(name + ": " + "onResume");
    }

    @Override
    public boolean hasOptionsMenu() {
        logs.add(name + ": " + "hasOptionsMenu");
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        logs.add(name + ": " + "onCreateOptionsMenu");
    }

    @Override
    public boolean onOptionsItemSelected(Object view, MenuItem item) {
        logs.add(name + ": " + "onOptionsItemSelected");
        return false;
    }

    @Override
    public void onPause(Object view) {
        logs.add(name + ": " + "onPause");
    }

    @Override
    public void onStop(Object view) {
        logs.add(name + ": " + "onStop");
    }

    @Override
    public void onSaveInstanceState(Object view, Bundle bundle) {
        logs.add(name + ": " + "onSaveInstanceState");
    }

    @Override
    public void onDestroy(Object view) {
        logs.add(name + ": " + "onDestroy");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        logs.add(name + ": " + "onActivityResult");
    }
}
