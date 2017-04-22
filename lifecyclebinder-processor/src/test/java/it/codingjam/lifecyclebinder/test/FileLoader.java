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

import org.truth0.Truth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.tools.JavaFileObject;

import it.codingjam.lifecyclebinder.LifeCycleBinderProcessor;

public class FileLoader {
    public static JavaFileObject loadClass(String source) {
        BufferedReader reader = null;
        try {
            File file = new File("test-data-lib/src/main/java/" + source.replace('.', '/') + ".java");
            reader = new BufferedReader(new FileReader(file));
            StringBuilder b = new StringBuilder();
            String s;
            while ((s = reader.readLine()) != null) {
                b.append(s).append("\n");
            }
            return JavaFileObjects.forSourceString(source, b.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static void check(String name) {
        check(name, name);
    }

    public static void check(String name, String expected) {
        checkSingleFile(name, expected + "$LifeCycleBinder");
    }

    private static void checkSingleFile(String name, String expected) {
        JavaFileObject target = loadClass(name);
        Truth.ASSERT.about(JavaSourceSubjectFactory.javaSource())
                .that(target)
                .processedWith(new LifeCycleBinderProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(loadClass(expected));
    }
}
