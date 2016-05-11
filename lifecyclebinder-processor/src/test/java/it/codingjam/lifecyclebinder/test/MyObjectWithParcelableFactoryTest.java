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

public class MyObjectWithParcelableFactoryTest {

    public static final String SOURCE =
            "package com.test;\n" +
                    "import android.content.Intent;\n" +
                    "import android.os.Bundle;\n" +
                    "import android.view.Menu;\n" +
                    "import android.view.MenuInflater;\n" +
                    "import android.view.MenuItem;\n" +
                    "import android.support.v4.app.FragmentActivity;\n" +
                    "import it.codingjam.lifecyclebinder.LifeCycleAware;\n" +
                    "import it.codingjam.lifecyclebinder.ViewLifeCycleAware;\n" +
                    "import it.codingjam.lifecyclebinder.InstanceState;\n" +
                    "import java.util.concurrent.Callable;\n" +
                    "class MyObjectWithParcelable implements ViewLifeCycleAware<MyView> {\n" +
                    "\n" +
                    "    @InstanceState\n" +
                    "    MyParcelable myParcelable;\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void onCreate(MyView view, Bundle bundle) {\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void onStart(MyView view) {\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void onResume(MyView view) {\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public boolean hasOptionsMenu() {\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public boolean onOptionsItemSelected(MyView view, MenuItem item) {\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void onPause(MyView view) {\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void onStop(MyView view) {\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void onSaveInstanceState(MyView view, Bundle bundle) {\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void onDestroy(MyView view) {\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void onActivityResult(int requestCode, int resultCode, Intent data) {\n" +
                    "    }\n" +
                    "}\n" +
                    "public class MyActivity extends FragmentActivity implements MyView  {\n" +
                    "    @LifeCycleAware(retained = true, name = \"myName\")\n" +
                    "    Callable<MyObjectWithParcelable> myObject = new Callable<MyObjectWithParcelable>() {;\n" +
                    "        @Override\n" +
                    "        public MyObjectWithParcelable call() throws Exception {\n" +
                    "            return new MyObjectWithParcelable();\n" +
                    "        }\n" +
                    "    };" +
                    "}";

    public static final String RESULT =
            "package com.test;\n" +
                    "\n" +
                    "import android.os.Bundle;\n" +
                    "import it.codingjam.lifecyclebinder.ObjectBinder;\n" +
                    "\n" +
                    "public final class MyActivity$LifeCycleBinder extends ObjectBinder<MyActivity, MyActivity> {\n" +
                    "  private MyObjectWithParcelable$LifeCycleBinder myObject = new MyObjectWithParcelable$LifeCycleBinder();\n" +
                    "\n" +
                    "  public void bind(MyActivity view) {\n" +
                    "    retainedObjectCallables.put(\"myName\", view.myObject);\n" +
                    "  }\n" +
                    "  public void saveInstanceState(MyActivity view, Bundle bundle) {\n" +
                    "    myObject.saveInstanceState((MyObjectWithParcelable) retainedObjects.get(\"myName\"), bundle);\n" +
                    "  }\n" +
                    "\n" +
                    "  public void restoreInstanceState(MyActivity view, Bundle bundle) {\n" +
                    "    myObject.restoreInstanceState((MyObjectWithParcelable) retainedObjects.get(\"myName\"), bundle);\n" +
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
