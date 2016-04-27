package it.codingjam.lifecyclebinder.test;

import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourceSubjectFactory;

import org.junit.Test;
import org.truth0.Truth;

import javax.tools.JavaFileObject;

import it.codingjam.lifecyclebinder.LifeCycleBinderProcessor;

public class Retained2Test {

    public static final String SOURCE =
            "package com.test;\n" +
                    "import android.support.v4.app.FragmentActivity;\n" +
                    "import it.codingjam.lifecyclebinder.LifeCycleAware;\n" +
                    "import java.util.concurrent.Callable;\n" +
                    "public class MyActivity extends FragmentActivity {\n" +
                    "    @LifeCycleAware(retained = true, name = \"myName\")\n" +
                    "    Callable<MyObject> myObject = new Callable<MyObject>() {\n" +
                    "        @Override\n" +
                    "        public MyObject call() throws Exception {\n" +
                    "            return new MyObject();\n" +
                    "        }\n" +
                    "    };\n" +
                    "    @LifeCycleAware(retained = true, name = \"myName2\")\n" +
                    "    Callable<MyObject> myObject2 = new Callable<MyObject>() {\n" +
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
                    "import it.codingjam.lifecyclebinder.ViewLifeCycleAware;\n" +
                    "import it.codingjam.lifecyclebinder.ViewLifeCycleAwareContainer;\n" +
                    "import java.lang.String;\n" +
                    "import java.util.Map;\n" +
                    "\n" +
                    "public final class MyActivity$LifeCycleBinder extends ObjectBinder<MyActivity> {\n" +
                    "  public void bind(MyActivity view, ViewLifeCycleAwareContainer container, Map<String, ViewLifeCycleAware> retainedObjects) {\n" +
                    "    ViewLifeCycleAware listener;\n" +
                    "    listener = retainedObjects.get(\"myName\");\n" +
                    "    if (listener == null) {\n" +
                    "      try {\n" +
                    "        listener = view.myObject.call();\n" +
                    "        retainedObjects.put(\"myName\", listener);\n" +
                    "      }\n" +
                    "      catch(Exception e) {\n" +
                    "        throw new RuntimeException(e);\n" +
                    "      }\n" +
                    "    }\n" +
                    "    container.addListener(listener);\n" +
                    "    listener = retainedObjects.get(\"myName2\");\n" +
                    "    if (listener == null) {\n" +
                    "      try {\n" +
                    "        listener = view.myObject2.call();\n" +
                    "        retainedObjects.put(\"myName2\", listener);\n" +
                    "      }\n" +
                    "      catch(Exception e) {\n" +
                    "        throw new RuntimeException(e);\n" +
                    "      }\n" +
                    "    }\n" +
                    "    container.addListener(listener);\n" +
                    "  }\n" +
                    "\n" +
                    "  public boolean containsRetainedObjects() {\n" +
                    "    return true;\n" +
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
