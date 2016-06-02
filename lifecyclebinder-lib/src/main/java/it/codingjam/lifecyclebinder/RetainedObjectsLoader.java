package it.codingjam.lifecyclebinder;

import android.content.Context;
import android.support.v4.content.Loader;

import java.util.HashMap;
import java.util.Map;

public class RetainedObjectsLoader extends Loader<Map<String, LifeCycleAware<?>>> {

    public Map<String, LifeCycleAware<?>> retainedObjects = new HashMap<>();

    public RetainedObjectsLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        deliverResult(retainedObjects);
    }

    @Override
    protected void onReset() {
        retainedObjects = null;
    }
}