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

public abstract class ObjectBinder<T, V> implements LifeCycleAware<V> {
    public void bind(LifeCycleAwareCollector<? extends V> collector, T view) {

    }

    @Override
    public void onCreate(V view, Bundle savedInstanceState, Intent intent, Bundle arguments) {
    }

    @Override
    public void onStart(V view) {
    }

    @Override
    public void onResume(V view) {
    }

    @Override
    public boolean hasOptionsMenu(V view) {
        return false;
    }

    @Override
    public void onCreateOptionsMenu(V view, Menu menu, MenuInflater inflater) {
    }

    @Override
    public boolean onOptionsItemSelected(V view, MenuItem item) {
        return false;
    }

    @Override
    public void onPause(V view) {
    }

    @Override
    public void onStop(V view) {
    }

    @Override
    public void onSaveInstanceState(V view, Bundle bundle) {
    }

    @Override
    public void onDestroy(V view, boolean changingConfigurations) {
    }

    @Override
    public void onActivityResult(V view, int requestCode, int resultCode, Intent data) {
    }

    @Override
    public void onViewCreated(V view, Bundle savedInstanceState) {
    }

    @Override
    public void onDestroyView(V view) {
    }
}
