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

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

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
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

@SupportedAnnotationTypes({
        "it.codingjam.lifecyclebinder.LifeCycleAware",
        "it.codingjam.lifecyclebinder.RetainedObjectProvider"
})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class LifeCycleBinderProcessor extends AbstractProcessor {

    public static final String LIFE_CYCLE_BINDER_SUFFIX = "$LifeCycleBinder";
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
        List<LifeCycleAwareInfo> elementsByClass = createLifeCycleAwareElements(
                roundEnv.getElementsAnnotatedWith(LifeCycleAware.class),
                roundEnv.getElementsAnnotatedWith(RetainedObjectProvider.class)
        );

        if (elementsByClass == null) {
            return true;
        }

        calculateNestedElements(elementsByClass);

        for (LifeCycleAwareInfo entry : elementsByClass) {
            generateBinder(entry);
        }
        return false;
    }

    private void calculateNestedElements(List<LifeCycleAwareInfo> elementsByClass) {
        for (LifeCycleAwareInfo lifeCycleAwareInfo : elementsByClass) {
            for (Element element : lifeCycleAwareInfo.lifeCycleAwareElements) {
                for (LifeCycleAwareInfo entry : elementsByClass) {
                    if (entry.element.asType().equals(element.asType())) {
                        lifeCycleAwareInfo.nestedElements.add(NestedLifeCycleAwareInfo.createNestedLifeCycleAwareInfo(element));
                    }
                }
            }
            TypeMirror superclass = lifeCycleAwareInfo.element.getSuperclass();
            for (LifeCycleAwareInfo entry : elementsByClass) {
                if (TypeUtils.isRawTypeEquals(superclass, entry.element.asType())) {
                    lifeCycleAwareInfo.nestedElements.add(NestedLifeCycleAwareInfo.createSuperclass(lifeCycleAwareInfo.element));
                    break;
                }
            }

            for (RetainedObjectInfo retainedEntry : lifeCycleAwareInfo.retainedObjects) {
                for (LifeCycleAwareInfo entry : elementsByClass) {
                    if (ClassName.get(entry.element.asType()).equals(retainedEntry.typeName)) {
                        lifeCycleAwareInfo.nestedElements.add(NestedLifeCycleAwareInfo.createRetainedObject(retainedEntry.field, retainedEntry));
                    }
                }
            }
        }
    }

    private List<LifeCycleAwareInfo> createLifeCycleAwareElements(Set<? extends Element> lifeCycleAwareElements, Set<? extends Element> retainedObjectElements) {
        Map<Element, LifeCycleAwareInfo> elementsByClass = new HashMap<>();

        for (Element element : lifeCycleAwareElements) {
            if (element.getKind() != ElementKind.FIELD) {
                error(element, "Only fields can be annotated with @%s", LifeCycleAware.class);
                return null;
            }

            VariableElement variable = (VariableElement) element;

            LifeCycleAware annotation = variable.getAnnotation(LifeCycleAware.class);

            TypeElement enclosingElement = (TypeElement) variable.getEnclosingElement();

            LifeCycleAwareInfo info = getLifeCycleAwareInfo(elementsByClass, enclosingElement);
            info.lifeCycleAwareElements.add(variable);
        }

        for (Element element : retainedObjectElements) {
            if (element.getKind() != ElementKind.FIELD) {
                error(element, "Only fields can be annotated with @%s", RetainedObjectProvider.class);
                return null;
            }

            VariableElement variable = (VariableElement) element;

            RetainedObjectProvider annotation = variable.getAnnotation(RetainedObjectProvider.class);

            TypeElement enclosingElement = (TypeElement) variable.getEnclosingElement();

            LifeCycleAwareInfo info = getLifeCycleAwareInfo(elementsByClass, enclosingElement);
            TypeName typeName = TypeUtils.getTypeArguments(variable.asType()).get(0);
            info.retainedObjects.add(new RetainedObjectInfo(variable.getSimpleName().toString(), variable, typeName, annotation.value()));
        }
        return new ArrayList<>(elementsByClass.values());
    }

    private LifeCycleAwareInfo getLifeCycleAwareInfo(Map<Element, LifeCycleAwareInfo> elementsByClass, TypeElement enclosingElement) {
        LifeCycleAwareInfo info = elementsByClass.get(enclosingElement);
        if (info == null) {
            info = new LifeCycleAwareInfo(enclosingElement);
            elementsByClass.put(enclosingElement, info);
        }
        return info;
    }

    private void generateBinder(LifeCycleAwareInfo lifeCycleAwareInfo) {
        TypeElement hostElement = lifeCycleAwareInfo.element;
        PackageElement packageElement = processingEnv.getElementUtils().getPackageOf(hostElement);
        final String simpleClassName = hostElement.getSimpleName().toString() + LIFE_CYCLE_BINDER_SUFFIX;
        final String qualifiedClassName = packageElement.getQualifiedName() + "." + simpleClassName;

        try {
            message(Diagnostic.Kind.NOTE, "writing class " + qualifiedClassName);
            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(
                    qualifiedClassName, lifeCycleAwareInfo.getLifeCycleAwareElementsArray());

            TypeName objectGenericType = TypeName.get(hostElement.asType());
            TypeName viewGenericType = getObjectBinderGenericTypeName(hostElement);

            TypeSpec.Builder builder = TypeSpec.classBuilder(simpleClassName)
                    .addModifiers(PUBLIC)
                    .superclass(ParameterizedTypeName.get(ClassName.get(ObjectBinder.class), objectGenericType, viewGenericType))
                    .addMethod(generateBindMethod(lifeCycleAwareInfo, objectGenericType));

            List<TypeName> typeArguments = TypeUtils.getTypeArguments(hostElement.asType());
            for (TypeName argument : typeArguments) {
                builder.addTypeVariable((TypeVariableName) argument);
            }

            for (NestedLifeCycleAwareInfo info : lifeCycleAwareInfo.nestedElements) {
                builder.addField(generateNestedBinderField(info));
            }

            writeFile(packageElement, sourceFile, builder.build());
        } catch (IOException e) {
            throw new RuntimeException("Failed writing class file " + qualifiedClassName, e);
        }
    }

    private void writeFile(PackageElement packageElement, JavaFileObject sourceFile, TypeSpec typeSpec) throws IOException {
        final Writer writer = sourceFile.openWriter();
        JavaFile.builder(packageElement.getQualifiedName().toString(), typeSpec).skipJavaLangImports(true)
                .build()
                .writeTo(writer);
        writer.close();
    }

    private FieldSpec generateNestedBinderField(NestedLifeCycleAwareInfo info) {
        TypeName className = info.getBinderClassName();
        return FieldSpec.builder(
                className,
                info.getFieldName(),
                PRIVATE
        ).initializer("new $T()", info.getBinderClassName()).build();
    }

    private MethodSpec generateBindMethod(LifeCycleAwareInfo lifeCycleAwareInfo, TypeName objectGenericType) {
        return MethodSpec.methodBuilder("bind")
                .addModifiers(PUBLIC)
                .returns(void.class)
                .addParameter(objectGenericType, "view", FINAL)
                .addCode(generateBindMethodBody(lifeCycleAwareInfo))
                .build();
    }

    private TypeName getObjectBinderGenericTypeName(TypeElement hostElement) {
        TypeName typeName = searchObjectBinderGenericTypeName(hostElement, null);
        if (typeName != null) {
            return typeName;
        } else {
            return TypeName.get(hostElement.asType());
        }
    }

    private TypeName searchObjectBinderGenericTypeName(TypeElement hostElement, List<TypeName> actualTypeArguments) {
        List<? extends TypeMirror> interfaces = hostElement.getInterfaces();
        for (TypeMirror type : interfaces) {
            TypeName typeName = TypeName.get(type);
            if (typeName instanceof ParameterizedTypeName) {
                ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) typeName;
                if (parameterizedTypeName.typeArguments.size() == 1 && parameterizedTypeName.rawType.equals(TypeName.get(ViewLifeCycleAware.class))) {
                    List<TypeName> formalTypeArguments = TypeUtils.getTypeArguments(hostElement.asType());
                    TypeName ret = parameterizedTypeName.typeArguments.get(0);
                    for (int i = 0; i < formalTypeArguments.size(); i++) {
                        if (formalTypeArguments.get(i).equals(ret)) {
                            if (actualTypeArguments != null) {
                                return actualTypeArguments.get(i);
                            }
                        }
                    }
                    return ret;
                }
            }
        }
        TypeMirror superclass = hostElement.getSuperclass();
        if (TypeName.get(superclass).equals(TypeName.get(Object.class))) {
            return null;
        } else {
            List<TypeName> superClassTypeElements = TypeUtils.getTypeArguments(superclass);
            return searchObjectBinderGenericTypeName((TypeElement) typeUtils.asElement(superclass), superClassTypeElements);
        }
    }

    private CodeBlock generateBindMethodBody(LifeCycleAwareInfo lifeCycleAwareInfo) {
        CodeBlock.Builder builder = CodeBlock.builder();
        for (Element element : lifeCycleAwareInfo.lifeCycleAwareElements) {
            builder.addStatement("listeners.add(view.$L)", element);
        }
        for (RetainedObjectInfo entry : lifeCycleAwareInfo.retainedObjects) {
            TypeName typeName = ParameterizedTypeName.get(entry.field.asType());
            if (!(typeName instanceof ParameterizedTypeName)) {
                //TODO error
            }
            ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) typeName;
            if (parameterizedTypeName.typeArguments.size() != 1) {
                //TODO error
            }
            Object argument;
            if (parameterizedTypeName.rawType.equals(TypeName.get(Callable.class))) {
                argument = "view." + entry.field;
            } else {
                TypeName returnTypeName = parameterizedTypeName.typeArguments.get(0);
                argument = TypeSpec.anonymousClassBuilder("")
                        .addSuperinterface(ParameterizedTypeName.get(ClassName.get(Callable.class), returnTypeName))
                        .addMethod(MethodSpec.methodBuilder("call")
                                .addAnnotation(Override.class)
                                .addModifiers(PUBLIC)
                                .addException(Exception.class)
                                .returns(returnTypeName)
                                .addStatement("return view.$L.get()", entry.field)
                                .build())
                        .build();
            }
            if (entry.fieldToPopulate != null && entry.fieldToPopulate.length() > 0) {
                builder.addStatement("view.$L = initRetainedObject($S, $L)", entry.fieldToPopulate, entry.name, argument);
                if (lifeCycleAwareInfo.isNested(entry)) {
                    builder.addStatement("$L.bind(view.$L)", entry.name, entry.fieldToPopulate);
                    builder.addStatement("listeners.addAll($L.getListeners())", entry.name);
                }
            } else {
                if (lifeCycleAwareInfo.isNested(entry)) {
                    builder.addStatement("$L.bind(($T) initRetainedObject($S, $L))", entry.name, entry.typeName, entry.name, argument);
                    builder.addStatement("listeners.addAll($L.getListeners())", entry.name);
                } else {
                    builder.addStatement("initRetainedObject($S, $L)", entry.name, argument);
                }
            }
        }
        for (NestedLifeCycleAwareInfo info : lifeCycleAwareInfo.nestedElements) {
            if (info.retained == null) {
                builder.addStatement("$L.bind($L)", info.getFieldName(), info.getBindMethodParameter());
                builder.addStatement("listeners.addAll($L.getListeners())", info.getFieldName());
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
