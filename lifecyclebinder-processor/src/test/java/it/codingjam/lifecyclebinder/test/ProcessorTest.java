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
        check("com.test.myActivity.MyActivity");
    }

    @Test
    public void testRetained() throws Exception {
        check("com.test.retained.ActivityWithRetained");
    }

    @Test
    public void testRetained2() throws Exception {
        check("com.test.retained.ActivityWithRetained2");
    }

    @Test
    public void testNestedObjects() throws Exception {
        check("com.test.nested.ActivityMyObjectWithParcelableAndInnerObject");
    }

    @Test
    public void testNestedObjects2() throws Exception {
        check("com.test.nested.ActivityMyObjectWithParcelableAndInnerObject",
                "com.test.nested.MyObjectWithParcelableAndInnerObject");
    }

    @Test
    public void testRetainedObjectsWithProviders() throws Exception {
        check("com.test.retainedObjectsWithProvider.ActivityWithRetainedProvider");
    }

    @Test
    public void testActivityWithBaseClass() throws Exception {
        check("com.test.activityWithBaseClass.MyActivityWithBaseClass");
    }

    @Test
    public void testObjectWithBaseClass() throws Exception {
        check("com.test.objectWithBaseClass.MyObjectWithBaseClass");
    }

    @Test
    public void testObjectWithGenericBaseClass() throws Exception {
        check("com.test.objectWithGenericBaseClass.MyObjectWithGenericBaseClass");
    }

    @Test
    public void testObjectWithNestedGenericBaseClass() throws Exception {
        check("com.test.objectWithNestedGenericBaseClass.MyObjectWithGenericBaseClass");
    }
}
