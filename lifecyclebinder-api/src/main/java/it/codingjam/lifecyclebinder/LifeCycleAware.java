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

public interface LifeCycleAware<T> {
    void onCreate(T view, Bundle savedInstanceState, Intent intent, Bundle arguments);

    void onStart(T view);

    void onResume(T view);

    boolean hasOptionsMenu();

    void onCreateOptionsMenu(Menu menu, MenuInflater inflater);

    boolean onOptionsItemSelected(T view, MenuItem item);

    void onPause(T view);

    void onStop(T view);

    void onSaveInstanceState(T view, Bundle bundle);

    void onDestroy(T view, boolean changingConfigurations);

    void onActivityResult(int requestCode, int resultCode, Intent data);
}
