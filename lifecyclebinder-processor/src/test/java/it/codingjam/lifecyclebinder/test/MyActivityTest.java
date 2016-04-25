package it.codingjam.lifecyclebinder.test;

import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourceSubjectFactory;

import org.junit.Test;
import org.truth0.Truth;

import javax.tools.JavaFileObject;

import it.codingjam.lifecyclebinder.LifeCycleBinderProcessor;

public class MyActivityTest {

    public static final String SOURCE =
            "package com.test;\n" +
                    "import android.support.v4.app.FragmentActivity;\n" +
                    "import it.codingjam.lifecyclebinder.LifeCycleAware;\n" +
                    "public class MyActivity extends FragmentActivity {\n" +
                    "    @LifeCycleAware\n" +
                    "    MyObject myObject;\n" +
                    "}";

    public static final String RESULT =
            "package com.test;\n" +
                    "\n" +
                    "import android.support.v4.app.FragmentManager;\n" +
                    "import it.codingjam.lifecyclebinder.ViewLifeCycleAwareContainer;\n" +
                    "\n" +
                    "public final class MyActivity$LifeCycleBinder {\n" +
                    "  public static void bind(MyActivity view, ViewLifeCycleAwareContainer container, FragmentManager activityFragmentManager) {" +
                    "    container.addListener(view.myObject);\n" +
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
