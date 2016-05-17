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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import it.codingjam.lifecyclebinder.LifeCycleAware;
import it.codingjam.lifecyclebinder.LifeCycleBinder;

public class MainActivity extends AppCompatActivity {

    public static final String LAYOUT = "layout";

    @LifeCycleAware
    public Logger logger = new Logger("MainActivity");

    @InstanceState
    MyParcelable myParcelable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getIntent().getIntExtra(LAYOUT, 0));

        if (savedInstanceState == null) {
            LifeCycleBinder.bind(savedInstanceState, this);
        }

        if (myParcelable == null) {
            myParcelable = new MyParcelable();
        }
    }
}
