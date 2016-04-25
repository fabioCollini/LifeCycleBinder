package it.codingjam.lifecyclebinder;

public interface ViewLifeCycleAwareContainer<T> {
    void addListener(ViewLifeCycleAware<T> listener);
}
