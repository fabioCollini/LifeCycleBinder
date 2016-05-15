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

public class ProcessorTest {
    @Test
    public void testMyActivity() throws Exception {
        check("com.test.MyActivity");
    }

    @Test
    public void testInstanceState() throws Exception {
        check("com.test.MyActivity3");
    }

    @Test
    public void testRetained() throws Exception {
        check("com.test.ActivityWithRetained");
    }

    @Test
    public void testRetained2() throws Exception {
        check("com.test.ActivityWithRetained2");
    }

    @Test
    public void testObjectWitParcelable() throws Exception {
        check("com.test.ActivityMyObjectWithParcelable");
    }

    @Test
    public void testObjectWitParcelableFactory() throws Exception {
        check("com.test.MyActivity2");
    }

    @Test
    public void testNestedObjects() throws Exception {
        check("com.test.ActivityMyObjectWithParcelableAndInnerObject");
    }

    @Test
    public void testNestedObjects2() throws Exception {
        check("com.test.ActivityMyObjectWithParcelableAndInnerObject",
                "com.test.MyObjectWithParcelableAndInnerObject");
    }

    @Test
    public void testRetainedObjectsWithProviders() throws Exception {
        check("com.test.ActivityWithRetainedProvider");
    }

    @Test
    public void testObjectWithBaseClass() throws Exception {
        check("com.test.MyActivityWithBaseClass");
    }
}
