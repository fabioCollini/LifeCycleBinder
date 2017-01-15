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
import com.squareup.javapoet.WildcardTypeName;
import it.codingjam.lifecyclebinder.data.LifeCycleAwareInfo;
import it.codingjam.lifecyclebinder.data.NestedLifeCycleAwareInfo;
import it.codingjam.lifecyclebinder.data.RetainedObjectInfo;
import it.codingjam.lifecyclebinder.utils.TypeUtils;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

public class BinderGenerator {
    public static final String LIFE_CYCLE_BINDER_SUFFIX = "$LifeCycleBinder";
    private static final String DELEGATE = "delegate";
    private ProcessingEnvironment processingEnv;
    private final Types typeUtils;
    private final Messager messager;

    public BinderGenerator(ProcessingEnvironment processingEnv, Types typeUtils, Messager messager) {
        this.processingEnv = processingEnv;
        this.typeUtils = typeUtils;
        this.messager = messager;
    }

    public void generateBinder(LifeCycleAwareInfo lifeCycleAwareInfo) {
        TypeElement hostElement = lifeCycleAwareInfo.element;
        PackageElement packageElement = processingEnv.getElementUtils().getPackageOf(hostElement);
        final String simpleClassName = hostElement.getSimpleName().toString() + LIFE_CYCLE_BINDER_SUFFIX;
        final String qualifiedClassName = packageElement.getQualifiedName() + "." + simpleClassName;

        try {
            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(
                    qualifiedClassName, lifeCycleAwareInfo.getLifeCycleAwareElementsArray());

            TypeName objectGenericType = TypeName.get(hostElement.asType());
            TypeName viewGenericType = getObjectBinderGenericTypeName(lifeCycleAwareInfo);

            TypeSpec.Builder builder = TypeSpec.classBuilder(simpleClassName)
                    .addModifiers(PUBLIC)
                    .addMethod(generateBindMethod(lifeCycleAwareInfo, objectGenericType));

            List<TypeName> typeArguments = TypeUtils.getTypeArguments(hostElement.asType());
            for (TypeName argument : typeArguments) {
                builder.addTypeVariable((TypeVariableName) argument);
            }

            manageEventsMethods(builder, lifeCycleAwareInfo, viewGenericType);

            writeFile(packageElement, sourceFile, builder.build());
        } catch (IOException e) {
            throw new RuntimeException("Failed writing class file " + qualifiedClassName, e);
        }
    }

    private void manageEventsMethods(TypeSpec.Builder builder, LifeCycleAwareInfo lifeCycleAwareInfo, TypeName viewGenericType) {
        if (!lifeCycleAwareInfo.eventsElements.isEmpty()) {
            builder.addField(FieldSpec.builder(TypeName.get(lifeCycleAwareInfo.element.asType()), DELEGATE, PRIVATE).build());

            for (ExecutableElement method : lifeCycleAwareInfo.eventsElements) {
                LifeCycleEvent[] events = method.getAnnotation(BindEvent.class).value();
                for (LifeCycleEvent event : events) {
                    generateEventDelegateMethod(builder, event, viewGenericType, method);
                }
            }
        }
    }

    private void generateEventDelegateMethod(TypeSpec.Builder builder, LifeCycleEvent event, TypeName viewGenericType, ExecutableElement method) {
        EventMethod eventMethod = EventMethod.EVENTS.get(event);
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(eventMethod.name)
                .addModifiers(PUBLIC)
                .addParameter(viewGenericType, "view");

        StringBuilder body = new StringBuilder();
        if (eventMethod.returnType != null) {
            methodBuilder.returns(eventMethod.returnType);
            body.append("return ");
        }
        body.append("delegate.$L(view");

        for (int i = 0; i < eventMethod.parameterTypes.length; i++) {
            TypeName parameterType = eventMethod.parameterTypes[i];
            methodBuilder = methodBuilder.addParameter(parameterType, "arg" + i);
            body.append(", ").append("arg").append(i);
        }
        body.append(");");
        methodBuilder.addCode(body.toString(), method.getSimpleName());

        builder.addMethod(methodBuilder.build());
    }

    private void writeFile(PackageElement packageElement, JavaFileObject sourceFile, TypeSpec typeSpec) throws IOException {
        final Writer writer = sourceFile.openWriter();
        JavaFile.builder(packageElement.getQualifiedName().toString(), typeSpec).skipJavaLangImports(true)
                .build()
                .writeTo(writer);
        writer.close();
    }

    private MethodSpec generateBindMethod(LifeCycleAwareInfo lifeCycleAwareInfo, TypeName objectGenericType) {
        ParameterizedTypeName collectorType = ParameterizedTypeName.get(
                ClassName.get(LifeCycleAwareCollector.class),
                WildcardTypeName.subtypeOf(getObjectBinderGenericTypeName(lifeCycleAwareInfo)));
        MethodSpec.Builder builder = MethodSpec.methodBuilder("bind")
                .addModifiers(PUBLIC, STATIC)
                .returns(void.class)
                .addParameter(collectorType, "collector")
                .addParameter(objectGenericType, "view", FINAL)
                .addCode(generateBindMethodBody(lifeCycleAwareInfo));

        for (TypeParameterElement typeParameterElement : lifeCycleAwareInfo.element.getTypeParameters()) {
            List<? extends TypeMirror> boundsElements = typeParameterElement.getBounds();
            TypeName[] bounds = new TypeName[boundsElements.size()];
            for (int i = 0; i < boundsElements.size(); i++) {
                bounds[i] = TypeName.get(boundsElements.get(i));
            }
            builder.addTypeVariable(TypeVariableName.get(typeParameterElement.getSimpleName().toString(), bounds));
        }
        return builder.build();
    }

    private TypeName getObjectBinderGenericTypeName(LifeCycleAwareInfo lifeCycleAwareInfo) {
        TypeElement hostElement = lifeCycleAwareInfo.element;
        TypeName typeName = searchObjectBinderGenericTypeName(hostElement, null);
        if (typeName != null) {
            return typeName;
        } else {
            if (lifeCycleAwareInfo.eventsElements.isEmpty()) {
                return TypeName.get(hostElement.asType());
            } else {
                return getViewTypeFromEventMethods(lifeCycleAwareInfo.eventsElements);
            }
        }
    }

    private TypeName getViewTypeFromEventMethods(List<ExecutableElement> eventsElements) {
        return TypeName.get(eventsElements.get(0).getParameters().get(0).asType());
    }

    private TypeName searchObjectBinderGenericTypeName(TypeElement hostElement, List<TypeName> actualTypeArguments) {
        List<? extends TypeMirror> interfaces = hostElement.getInterfaces();
        for (TypeMirror type : interfaces) {
            TypeName typeName = TypeName.get(type);
            if (typeName instanceof ParameterizedTypeName) {
                ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) typeName;
                if (parameterizedTypeName.typeArguments.size() == 1 && parameterizedTypeName.rawType.equals(TypeName.get(LifeCycleAware.class))) {
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
            builder = builder
                    .addStatement("collector.addLifeCycleAware(view.$L)", element);
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
                if (!lifeCycleAwareInfo.containsField(entry.fieldToPopulate, typeUtils)) {
                    error(entry.field, "Field %s not found, it's referenced in field %s", entry.fieldToPopulate, entry.field);
                }
                builder.addStatement("view.$L = collector.addRetainedFactory($S, $L, false)", entry.fieldToPopulate, entry.name, argument);
                if (lifeCycleAwareInfo.isNested(entry)) {
                    builder.addStatement("$T.bind(collector, view.$L)", entry.binderClassName, entry.fieldToPopulate);
                }
                builder.addStatement("collector.addLifeCycleAware(view.$L)", entry.fieldToPopulate);
            } else {
                if (lifeCycleAwareInfo.isNested(entry)) {
                    builder.addStatement("$T.bind(collector, collector.addRetainedFactory($S, $L, true))", entry.binderClassName, entry.name,
                            argument);
                } else {
                    builder.addStatement("collector.addRetainedFactory($S, $L, true)", entry.name, argument);
                }
            }
        }
        for (NestedLifeCycleAwareInfo info : lifeCycleAwareInfo.nestedElements) {
            if (info.retained == null) {
                builder.addStatement("$T.bind(collector, $L)", info.getBinderClassName(), info.getBindMethodParameter());
            }
        }

        if (!lifeCycleAwareInfo.eventsElements.isEmpty()) {
            builder.addStatement("this.delegate = view");
            builder.addStatement("collector.addLifeCycleAware(this)");
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
