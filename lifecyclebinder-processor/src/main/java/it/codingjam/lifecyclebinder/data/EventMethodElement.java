package it.codingjam.lifecyclebinder.data;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import it.codingjam.lifecyclebinder.BindEvent;
import it.codingjam.lifecyclebinder.EventMethodDefinition;
import it.codingjam.lifecyclebinder.LifeCycleEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class EventMethodElement {
    private static final ClassName EVENT_CLASS_NAME = ClassName.get(LifeCycleEvent.class);
    public final ExecutableElement element;
    public final LifeCycleEvent[] events;
    public final TypeName viewTypeName;

    public EventMethodElement(ExecutableElement element) {
        this.element = element;
        events = element.getAnnotation(BindEvent.class).value();
        viewTypeName = retrieveViewTypeName(element);
    }

    private TypeName retrieveViewTypeName(ExecutableElement element) {
        for (LifeCycleEvent lifeCycleEvent : events) {
            EventMethodDefinition definition = EventMethodDefinition.EVENTS.get(lifeCycleEvent);
            LinkedList<VariableElement> parameters = new LinkedList<>(element.getParameters());
            int size = parameters.size();
            if (size == 0) {
                return null;
            } else {
                if (definition.parameterTypes.length > 0) {
                    if (ClassName.get(parameters.getLast().asType()).equals(definition.parameterTypes[definition.parameterTypes.length - 1])) {
                        for (int i = 0; i < definition.parameterTypes.length; i++) {
                            parameters.removeLast();
                        }
                    }
                }
                if (!parameters.isEmpty()) {
                    if (ClassName.get(parameters.getLast().asType()).equals(ClassName.get(LifeCycleEvent.class))) {
                        parameters.removeLast();
                    }
                }
                if (!parameters.isEmpty()) {
                    return ClassName.get(parameters.getFirst().asType());
                }
            }
        }
        return null;
    }

    public boolean isVoidReturn() {
        return ClassName.get(element.getReturnType()).equals(ClassName.VOID);
    }

    public void createBody(MethodSpec.Builder methodBuilder, LifeCycleEvent event, TypeName viewGenericType, EventMethodDefinition definition) {
        StringBuilder body = new StringBuilder();
        if (definition.returnType != null) {
            if (!isVoidReturn()) {
                body.append("return ");
            }
        }
        List<String> args = new ArrayList<>();
        List<? extends VariableElement> parameters = element.getParameters();
        boolean addEventType = false;
        if (!parameters.isEmpty()) {
            int pos = 0;
            if (ClassName.get(parameters.get(pos).asType()).equals(viewGenericType)) {
                args.add("argView");
                pos++;
            }
            if (parameters.size() - pos > 0) {
                if (ClassName.get(parameters.get(pos).asType()).equals(EVENT_CLASS_NAME)) {
                    args.add("$T." + event.toString());
                    addEventType = true;
                    pos++;
                }
                if (parameters.size() - pos > 0) {
                    for (int i = 0; i < definition.parameterTypes.length; i++) {
                        args.add("arg" + i);
                    }
                }
            }
        }
        body.append("lifeCycleAware.$L(").append(listToString(args)).append(")");
        if (addEventType) {
            methodBuilder.addStatement(body.toString(), element.getSimpleName(), EVENT_CLASS_NAME);
        } else {
            methodBuilder.addStatement(body.toString(), element.getSimpleName());
        }

        if (definition.returnType != null && isVoidReturn()) {
            methodBuilder.addStatement("return true");
        }
    }

    private String listToString(List<String> args) {
        StringBuilder b = new StringBuilder();
        for (String arg : args) {
            if (b.length() > 0) {
                b.append(", ");
            }
            b.append(arg);
        }
        return b.toString();
    }
}
