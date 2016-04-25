package it.codingjam.lifecyclebinder;

import android.support.annotation.NonNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class ReflectionUtils {
    @NonNull
    public static <T> List<ViewLifeCycleAware<T>> loadListeners(Object activity) {
        List<ViewLifeCycleAware<T>> listeners = new ArrayList<>();
        for (Field field : getFields(activity, false)) {
            ViewLifeCycleAware<T> fieldValue = getFieldValue(activity, field);
            listeners.add(fieldValue);
        }
        return listeners;
    }

    static <T> T getFieldValue(Object activity, Field field) {
        try {
            return (T) field.get(activity);
        } catch (Exception e) {
            throw new RuntimeException("Error accessing field " + field, e);
        }
    }

    @NonNull
    static List<Field> getFields(Object obj, boolean retained) {
        List<Field> annotatedFields = new ArrayList<>();
        Field[] fields = obj.getClass().getFields();
        for (Field field : fields) {
            LifeCycleAware annotation = field.getAnnotation(LifeCycleAware.class);
            if (annotation != null && annotation.retained() == retained) {
                annotatedFields.add(field);
            }
        }
        return annotatedFields;
    }

    @NonNull
    public static <T> Map<String, Callable<ViewLifeCycleAware<T>>> loadRetainedListeners(Object obj) {
        Map<String, Callable<ViewLifeCycleAware<T>>> map = new HashMap<>();
        List<Field> fields = getFields(obj, true);
        if (!fields.isEmpty()) {
            for (Field field : fields) {
                String name = field.getAnnotation(LifeCycleAware.class).name();
                Callable<ViewLifeCycleAware<T>> callable = getFieldValue(obj, field);
                map.put(name, callable);
            }
        }
        return map;
    }
}
