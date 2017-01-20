package it.codingjam.lifecyclebinder.data;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import it.codingjam.lifecyclebinder.BindEvent;
import it.codingjam.lifecyclebinder.EventMethodDefinition;
import it.codingjam.lifecyclebinder.LifeCycleEvent;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
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
        List<? extends VariableElement> parameters = element.getParameters();
        for (LifeCycleEvent lifeCycleEvent : events) {
            EventMethodDefinition definition = EventMethodDefinition.EVENTS.get(lifeCycleEvent);
            int size = parameters.size();
            if (size == 0 || size == definition.parameterTypes.length) {
                return null;
            } else if (size > definition.parameterTypes.length) {
                TypeName ret = ClassName.get(parameters.get(0).asType());
                if (!ret.equals(ClassName.get(LifeCycleEvent.class))) {
                    return ret;
                }
            }
        }
        return null;
    }

    public Name getSimpleName() {
        return element.getSimpleName();
    }

    public boolean containsLifecycleParameters(LifeCycleEvent event) {
        EventMethodDefinition definition = EventMethodDefinition.EVENTS.get(event);
        List<? extends VariableElement> parameters = element.getParameters();
        int size = parameters.size();
        return size >= definition.parameterTypes.length;
    }

    public boolean isVoidReturn() {
        return ClassName.get(element.getReturnType()).equals(ClassName.VOID);
    }

    public void createBody(MethodSpec.Builder methodBuilder, LifeCycleEvent event, TypeName viewGenericType,
            EventMethodDefinition eventMethodDefinition, StringBuilder body) {
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
                    for (int i = 0; i < eventMethodDefinition.parameterTypes.length; i++) {
                        args.add("arg" + i);
                    }
                }
            }
        }
        body.append("view.$L(").append(listToString(args)).append(")");
        if (addEventType) {
            methodBuilder.addStatement(body.toString(), element.getSimpleName(), EVENT_CLASS_NAME);
        } else {
            methodBuilder.addStatement(body.toString(), element.getSimpleName());
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
