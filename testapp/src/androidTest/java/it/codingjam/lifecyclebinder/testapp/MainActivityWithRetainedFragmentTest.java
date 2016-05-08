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

import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class MainActivityWithRetainedFragmentTest {
    @Rule public MyRule<MainActivity> rule = new MyRule<>(MainActivity.class);

    @Test
    public void testCreate() throws Exception {
        rule.launchActivity(MainActivity.LAYOUT, R.layout.activity_with_retained_fragment);

        assertThat(Logger.ALL_LOGS).containsExactly(
                "MyRetainedFragment2: onCreate",
                "MainActivity1: onCreate",
                "MyRetainedFragment2: onStart",
                "MainActivity1: onStart",
                "MyRetainedFragment2: onResume",
                "MyRetainedFragment2: hasOptionsMenu",
                "MainActivity1: onResume",
                "MainActivity1: hasOptionsMenu"
        );
    }

    @Test
    public void testOrientationChange() throws Exception {
        rule.launchActivity(MainActivity.LAYOUT, R.layout.activity_with_retained_fragment);

        rule.rotateScreen();

        assertThat(Logger.ALL_LOGS).containsExactly(
                "MyRetainedFragment2: onCreate",
                "MainActivity1: onCreate",
                "MyRetainedFragment2: onStart",
                "MainActivity1: onStart",
                "MyRetainedFragment2: onResume",
                "MyRetainedFragment2: hasOptionsMenu",
                "MainActivity1: onResume",
                "MainActivity1: hasOptionsMenu",
                "MyRetainedFragment2: onPause",
                "MainActivity1: onPause",
                "MyRetainedFragment2: onSaveInstanceState",
                "MainActivity1: onSaveInstanceState",
                "MyRetainedFragment2: onStop",
                "MainActivity1: onStop",
                "MyRetainedFragment2: onDestroy",
                "MainActivity1: onDestroy",
                "MainActivity3: onCreate",
                "MyRetainedFragment2: onCreate",
                "MyRetainedFragment2: onStart",
                "MainActivity3: onStart",
                "MyRetainedFragment2: onResume",
                "MyRetainedFragment2: hasOptionsMenu",
                "MainActivity3: onResume",
                "MainActivity3: hasOptionsMenu"
        );
    }
}
