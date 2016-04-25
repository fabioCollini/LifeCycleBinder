package it.codingjam.lifecyclebinder;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.HashMap;
import java.util.Map;

public class LifeCycleRetainedFragment extends Fragment {
    private static final String RETAINED_FRAGMENT = "_LIFE_CYCLE_RETAINED_FRAGMENT_";

    public final Map<String, ViewLifeCycleAware<?>> map = new HashMap<>();

    public LifeCycleRetainedFragment() {
        setRetainInstance(true);
    }

    @NonNull
    public static LifeCycleRetainedFragment getOrCreateRetainedFragment(FragmentManager fragmentManager) {
        LifeCycleRetainedFragment fragment = (LifeCycleRetainedFragment) fragmentManager.findFragmentByTag(RETAINED_FRAGMENT);

        if (fragment == null) {
            fragment = new LifeCycleRetainedFragment();
            fragmentManager.beginTransaction().add(fragment, RETAINED_FRAGMENT).commit();
        }
        return fragment;
    }
}
