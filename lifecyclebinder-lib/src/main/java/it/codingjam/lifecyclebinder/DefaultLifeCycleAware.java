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

package it.codingjam.lifecyclebinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class DefaultLifeCycleAware<T> implements LifeCycleAware<T> {
    @Override
    public void onCreate(T view, Bundle savedInstanceState, Intent intent, Bundle arguments) {
    }

    @Override
    public void onStart(T view) {
    }

    @Override
    public void onResume(T view) {
    }

    @Override
    public boolean hasOptionsMenu(T view) {
        return false;
    }

    @Override
    public void onCreateOptionsMenu(T view, Menu menu, MenuInflater inflater) {
    }

    @Override
    public boolean onOptionsItemSelected(T view, MenuItem item) {
        return false;
    }

    @Override
    public void onPause(T view) {
    }

    @Override
    public void onStop(T view) {
    }

    @Override
    public void onSaveInstanceState(T view, Bundle bundle) {
    }

    @Override
    public void onDestroy(T view, boolean changingConfigurations) {
    }

    @Override
    public void onActivityResult(T view, int requestCode, int resultCode, Intent data) {
    }
}
