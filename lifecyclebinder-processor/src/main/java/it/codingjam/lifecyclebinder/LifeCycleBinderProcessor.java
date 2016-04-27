package it.codingjam.lifecyclebinder;

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

@SupportedAnnotationTypes("it.codingjam.lifecyclebinder.LifeCycleAware")
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
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(LifeCycleAware.class);

        message(Diagnostic.Kind.NOTE, "processing " + elements.size() + " fields");

        if (elements.isEmpty()) {
            return true;
        }

        Map<Element, ListenersInfo> elementsByClass = new HashMap<>();

        for (Element element : elements) {
            if (element.getKind() != ElementKind.FIELD) {
                error(element, "Only fields can be annotated with @%s", LifeCycleAware.class);
                return true;
            }

            VariableElement variable = (VariableElement) element;

            LifeCycleAware annotation = variable.getAnnotation(LifeCycleAware.class);

            Element enclosingElement = variable.getEnclosingElement();

            ListenersInfo info = elementsByClass.get(enclosingElement);
            if (info == null) {
                info = new ListenersInfo();
                elementsByClass.put(enclosingElement, info);
            }
            if (!annotation.retained()) {
                info.elements.add(variable);
            } else {
                info.retainedObjects.put(annotation.name(), variable);
            }
        }

        for (Map.Entry<Element, ListenersInfo> entry : elementsByClass.entrySet()) {
            generateBinder(entry.getValue(), entry.getKey());
        }
        return false;
    }

    private static class ListenersInfo {
        public final List<Element> elements = new ArrayList<>();

        public final TreeMap<String, Element> retainedObjects = new TreeMap<>();
    }

    private void generateBinder(ListenersInfo listenersInfo, Element hostElement) {
        PackageElement packageElement = processingEnv.getElementUtils().getPackageOf(hostElement);
        final String simpleClassName = hostElement.getSimpleName().toString() + "$LifeCycleBinder";
        final String qualifiedClassName = packageElement.getQualifiedName() + "." + simpleClassName;

        try {
            message(Diagnostic.Kind.NOTE, "writing class " + qualifiedClassName);
            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(
                    qualifiedClassName, listenersInfo.elements.toArray(new Element[listenersInfo.elements.size()]));

            MethodSpec bindMethod = MethodSpec.methodBuilder("bind")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addParameter(TypeName.get(hostElement.asType()), "view")
                    .addParameter(ViewLifeCycleAwareContainer.class, "container")
                    .addParameter(ParameterizedTypeName.get(Map.class, String.class, ViewLifeCycleAware.class), "retainedObjects")
                    .addCode(generateBindMethod(hostElement, listenersInfo))
                    .build();

            MethodSpec containsMethod = MethodSpec.methodBuilder("containsRetainedObjects")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(Boolean.TYPE)
                    .addStatement("return $L", !listenersInfo.retainedObjects.isEmpty())
                    .build();

            TypeSpec classType = TypeSpec.classBuilder(simpleClassName)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(bindMethod)
                    .addMethod(containsMethod)
                    .superclass(ParameterizedTypeName.get(ClassName.get(ObjectBinder.class), TypeName.get(hostElement.asType())))
                    .build();

            final Writer writer = sourceFile.openWriter();
            JavaFile.builder(packageElement.getQualifiedName().toString(), classType)
                    .build()
                    .writeTo(writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed writing class file " + qualifiedClassName, e);
        }
    }

    private CodeBlock generateBindMethod(Element hostElement, ListenersInfo listenersInfo) {
        CodeBlock.Builder builder = CodeBlock.builder();
        for (Element element : listenersInfo.elements) {
            builder.addStatement("container.addListener(view.$L)", element);
        }
        if (!listenersInfo.retainedObjects.isEmpty()) {
            builder.addStatement("ViewLifeCycleAware listener");
            Set<Map.Entry<String, Element>> entries = listenersInfo.retainedObjects.entrySet();
            for (Map.Entry<String, Element> entry : entries) {
                builder
                        .addStatement("listener = retainedObjects.get($S)", entry.getKey())
                        .beginControlFlow("if (listener == null)")
                        .beginControlFlow("try")
                        .addStatement("listener = view.$L.call()", entry.getValue())
                        .addStatement("retainedObjects.put($S, listener)", entry.getKey())
                        .endControlFlow()
                        .beginControlFlow("catch(Exception e)")
                        .addStatement("throw new RuntimeException(e)")
                        .endControlFlow()
                        .endControlFlow()
                        .addStatement("container.addListener(listener)");
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
