package it.codingjam.lifecyclebinder.data;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import it.codingjam.lifecyclebinder.BindEvent;
import it.codingjam.lifecyclebinder.EventMethodDefinition;
import it.codingjam.lifecyclebinder.LifeCycleEvent;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;

public class EventMethodElement {
    public final ExecutableElement element;
    public final LifeCycleEvent[] events;
    public final TypeName viewTypeName;

    public EventMethodElement(ExecutableElement element) {
        this.element = element;
        events = element.getAnnotation(BindEvent.class).value();
        viewTypeName = retrieveViewtypeName(element);
    }

    private TypeName retrieveViewtypeName(ExecutableElement element) {
        List<? extends VariableElement> parameters = element.getParameters();
        for (LifeCycleEvent lifeCycleEvent : events) {
            EventMethodDefinition definition = EventMethodDefinition.EVENTS.get(lifeCycleEvent);
            int size = parameters.size();
            if (size == 0 || size == definition.parameterTypes.length) {
                return null;
            } else if (size == definition.parameterTypes.length + 1) {
                return ClassName.get(parameters.get(0).asType());
            }
        }
        return null;
    }

    public Name getSimpleName() {
        return element.getSimpleName();
    }

    public boolean containsViewParameter(TypeName viewGenericType) {
        List<? extends VariableElement> parameters = element.getParameters();
        return !parameters.isEmpty() && ClassName.get(parameters.get(0).asType()).equals(viewGenericType);
    }
}
