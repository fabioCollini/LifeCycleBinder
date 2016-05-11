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

import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourceSubjectFactory;

import org.junit.Test;
import org.truth0.Truth;

import javax.tools.JavaFileObject;

import it.codingjam.lifecyclebinder.LifeCycleBinderProcessor;

public class RetainedTest {

    public static final String SOURCE =
            "package com.test;\n" +
                    "import android.support.v4.app.FragmentActivity;\n" +
                    "import it.codingjam.lifecyclebinder.LifeCycleAware;\n" +
                    "import java.util.concurrent.Callable;\n" +
                    "public class MyActivity extends FragmentActivity implements MyView {\n" +
                    "    @LifeCycleAware(retained = true, name = \"myName\")\n" +
                    "    Callable<MyObject> myObject = new Callable<MyObject>() {\n" +
                    "        @Override\n" +
                    "        public MyObject call() throws Exception {\n" +
                    "            return new MyObject();\n" +
                    "        }\n" +
                    "    };\n" +
                    "}";

    public static final String RESULT =
            "package com.test;\n" +
                    "\n" +
                    "import it.codingjam.lifecyclebinder.ObjectBinder;\n" +
                    "\n" +
                    "public final class MyActivity$LifeCycleBinder extends ObjectBinder<MyActivity, MyActivity> {\n" +
                    "  public void bind(MyActivity view) {\n" +
                    "    retainedObjectCallables.put(\"myName\", view.myObject);\n" +
                    "  }\n" +
                    "}";

    @Test
    public void testMyActivity() throws Exception {
        JavaFileObject expectedSource = JavaFileObjects.forSourceString("com.test.MyActivity$LifeCycleBinder", RESULT);
        JavaFileObject target = JavaFileObjects.forSourceString("com.test.MyActivity", SOURCE);
        Truth.ASSERT.about(JavaSourceSubjectFactory.javaSource())
                .that(target)
                .processedWith(new LifeCycleBinderProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedSource);


    }
}
