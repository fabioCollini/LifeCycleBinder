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

package it.codingjam.lifecyclebinder;

import android.os.Bundle;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes({
        "it.codingjam.lifecyclebinder.LifeCycleAware",
        "it.codingjam.lifecyclebinder.InstanceState"
})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class LifeCycleBinderProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<Element, LifeCycleAwareInfo> elementsByClass = createLifeCycleAwareElementsMap(
                roundEnv.getElementsAnnotatedWith(LifeCycleAware.class),
                roundEnv.getElementsAnnotatedWith(InstanceState.class)
        );

        if (elementsByClass == null) {
            return true;
        }
        for (Map.Entry<Element, LifeCycleAwareInfo> entry : elementsByClass.entrySet()) {
            generateBinder(entry.getValue(), entry.getKey());
        }
        return false;
    }

    private Map<Element, LifeCycleAwareInfo> createLifeCycleAwareElementsMap(Set<? extends Element> lifeCycleAwareElements, Set<? extends Element> instanceStateElements) {
        Map<Element, LifeCycleAwareInfo> elementsByClass = new HashMap<>();

        for (Element element : lifeCycleAwareElements) {
            if (element.getKind() != ElementKind.FIELD) {
                error(element, "Only fields can be annotated with @%s", LifeCycleAware.class);
                return null;
            }

            VariableElement variable = (VariableElement) element;

            LifeCycleAware annotation = variable.getAnnotation(LifeCycleAware.class);

            Element enclosingElement = variable.getEnclosingElement();

            LifeCycleAwareInfo info = getLifeCycleAwareInfo(elementsByClass, enclosingElement);
            if (!annotation.retained()) {
                info.lifeCycleAwareElements.add(variable);
            } else {
                info.retainedObjects.put(annotation.name(), variable);
            }
        }
        for (Element element : instanceStateElements) {
            VariableElement variable = (VariableElement) element;
            Element enclosingElement = variable.getEnclosingElement();
            LifeCycleAwareInfo info = getLifeCycleAwareInfo(elementsByClass, enclosingElement);
            info.instanceStateElements.add(element);
        }
        return elementsByClass;
    }

    private LifeCycleAwareInfo getLifeCycleAwareInfo(Map<Element, LifeCycleAwareInfo> elementsByClass, Element enclosingElement) {
        LifeCycleAwareInfo info = elementsByClass.get(enclosingElement);
        if (info == null) {
            info = new LifeCycleAwareInfo();
            elementsByClass.put(enclosingElement, info);
        }
        return info;
    }

    private static class LifeCycleAwareInfo {
        public final List<Element> lifeCycleAwareElements = new ArrayList<>();

        public final List<Element> instanceStateElements = new ArrayList<>();

        public final TreeMap<String, Element> retainedObjects = new TreeMap<>();
    }

    private void generateBinder(LifeCycleAwareInfo lifeCycleAwareInfo, Element hostElement) {
        PackageElement packageElement = processingEnv.getElementUtils().getPackageOf(hostElement);
        final String simpleClassName = hostElement.getSimpleName().toString() + "$LifeCycleBinder";
        final String qualifiedClassName = packageElement.getQualifiedName() + "." + simpleClassName;

        try {
            message(Diagnostic.Kind.NOTE, "writing class " + qualifiedClassName);
            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(
                    qualifiedClassName, lifeCycleAwareInfo.lifeCycleAwareElements.toArray(new Element[lifeCycleAwareInfo.lifeCycleAwareElements.size()]));

            MethodSpec bindMethod = MethodSpec.methodBuilder("bind")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addParameter(TypeName.get(hostElement.asType()), "view")
                    .addCode(generateBindMethod(lifeCycleAwareInfo))
                    .build();

            TypeSpec.Builder builder = TypeSpec.classBuilder(simpleClassName)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(bindMethod)
                    .superclass(ParameterizedTypeName.get(ClassName.get(ObjectBinder.class), TypeName.get(hostElement.asType())));

            if (!lifeCycleAwareInfo.instanceStateElements.isEmpty()) {
                builder = builder
                        .addMethod(createSaveInstanceStateMethod(hostElement, lifeCycleAwareInfo.instanceStateElements))
                        .addMethod(createRestoreInstanceStateMethod(hostElement, lifeCycleAwareInfo.instanceStateElements));
            }

            TypeSpec classType = builder.build();

            final Writer writer = sourceFile.openWriter();
            JavaFile.builder(packageElement.getQualifiedName().toString(), classType)
                    .build()
                    .writeTo(writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed writing class file " + qualifiedClassName, e);
        }
    }

    private MethodSpec createRestoreInstanceStateMethod(Element hostElement, List<Element> instanceStateElements) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("restoreInstanceState")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(TypeName.get(hostElement.asType()), "view")
                .addParameter(Bundle.class, "bundle");
        for (Element element : instanceStateElements) {
            builder.addStatement("view.$L = bundle.getParcelable($S)", element.getSimpleName(), element.getSimpleName());
        }
        return builder.build();
    }

    private MethodSpec createSaveInstanceStateMethod(Element hostElement, List<Element> instanceStateElements) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("saveInstanceState")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(TypeName.get(hostElement.asType()), "view")
                .addParameter(Bundle.class, "bundle");
        for (Element element : instanceStateElements) {
            builder.addStatement("bundle.putParcelable($S, view.$L)", element.getSimpleName(), element.getSimpleName());
        }
        return builder.build();
    }

    private CodeBlock generateBindMethod(LifeCycleAwareInfo lifeCycleAwareInfo) {
        CodeBlock.Builder builder = CodeBlock.builder();
        for (Element element : lifeCycleAwareInfo.lifeCycleAwareElements) {
            builder.addStatement("listeners.add(view.$L)", element);
        }
        if (!lifeCycleAwareInfo.retainedObjects.isEmpty()) {
            Set<Map.Entry<String, Element>> entries = lifeCycleAwareInfo.retainedObjects.entrySet();
            for (Map.Entry<String, Element> entry : entries) {
                builder.addStatement("retainedObjectCallables.put($S, view.$L)", entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }

    private void message(Diagnostic.Kind kind, String message) {
        messager.printMessage(kind, "LifeCycleBinder: " + message);
    }
}
