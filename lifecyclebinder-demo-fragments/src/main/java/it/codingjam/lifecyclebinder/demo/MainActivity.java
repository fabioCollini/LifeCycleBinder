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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import it.codingjam.lifecyclebinder.BindLifeCycle;
import it.codingjam.lifecyclebinder.LifeCycleBinder;

public class MainActivity extends AppCompatActivity {

    @BindLifeCycle ActivityLogger activityLogger = new ActivityLogger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LifeCycleBinder.bind(this);

        findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment newFragment = fragmentManager.findFragmentByTag("newFragment");
                if (newFragment == null) {
                    fragmentManager.beginTransaction().add(R.id.container, new MyFragment(), "newFragment").commit();
                } else {
                    fragmentManager.beginTransaction().remove(newFragment).commit();
                }
            }
        });
    }
}
