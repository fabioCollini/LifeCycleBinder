package it.codingjam.lifecyclebinder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.Map;

public class LifeCycleBinder {
    public static void bind(Fragment fragment) {
        bind(fragment, fragment.getChildFragmentManager(), fragment.getActivity().getSupportFragmentManager());
    }

    public static void bind(FragmentActivity activity) {
        bind(activity, activity.getSupportFragmentManager(), activity.getSupportFragmentManager());
    }

    private static <T> void bind(T obj, FragmentManager fragmentManager, FragmentManager activityFragmentManager) {
        LifeCycleBinderFragment<T> fragment = LifeCycleBinderFragment.getOrCreate(fragmentManager);
        fragment.init(obj);
        try {
            Class<?> c = Class.forName(obj.getClass().getName() + "$LifeCycleBinder");
            ObjectBinder<T> objectBinder = (ObjectBinder<T>) c.newInstance();
            Map<String, Object> retainedObjects = null;
            if (objectBinder.containsRetainedObjects()) {
                retainedObjects = (Map) LifeCycleRetainedFragment.getOrCreateRetainedFragment(activityFragmentManager).map;
            }
            objectBinder.bind(obj, fragment, retainedObjects);
        } catch (ClassNotFoundException ignored) {
        } catch (Exception e) {
            throw new RuntimeException("Error invoking binding", e);
        }
    }
}
