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

package it.codingjam.lifecyclebinder.test;

import org.junit.Test;

import static it.codingjam.lifecyclebinder.test.FileLoader.check;

public class ProcessorEventsTest {
    @Test public void testObjectWithEvents() throws Exception {
        check("com.test.MyObjectWithEvents");
    }

    @Test public void testMyActivityWithEvents() throws Exception {
        check("com.test.myActivityEvents.MyActivity");
    }

    @Test public void testRetained() throws Exception {
        check("com.test.retainedEvents.ActivityWithRetained");
    }

    @Test public void testRetainedWithField() throws Exception {
        check("com.test.retainedEvents.ActivityWithRetainedAndField");
    }

    @Test public void testMyActivityWithEventsNoViewParam() throws Exception {
        check("com.test.myActivityEventsNoViewParam.MyObjectWithEvents");
    }
}
