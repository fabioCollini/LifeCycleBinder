package it.codingjam.lifecyclebinder;

import java.util.Map;

public abstract class ObjectBinder<T> {
    public abstract void bind(T view, ViewLifeCycleAwareContainer container, Map<String, ViewLifeCycleAware> retainedObjects);

    public abstract boolean containsRetainedObjects();
}
