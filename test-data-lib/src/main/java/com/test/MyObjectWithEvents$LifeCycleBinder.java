package com.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import it.codingjam.lifecyclebinder.DefaultLifeCycleAware;
import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;

public class MyObjectWithEvents$LifeCycleBinder {

    public static MyObjectWithEvents bind(LifeCycleAwareCollector collector, final MyObjectWithEvents lifeCycleAware, boolean addInList) {
        if (addInList) {
            collector.addLifeCycleAware(new DefaultLifeCycleAware<MyView>() {
                public void onCreate(MyView argView, Bundle arg0, Intent arg1, Bundle arg2) {
                    lifeCycleAware.myOnCreate(argView, arg0, arg1, arg2);
                }

                public void onStart(MyView argView) {
                    lifeCycleAware.myOnStart(argView);
                }

                public void onResume(MyView argView) {
                    lifeCycleAware.myOnResume(argView);
                }

                public boolean hasOptionsMenu(MyView argView) {
                    return lifeCycleAware.myHasOptionsMenu(argView);
                }

                public void onCreateOptionsMenu(MyView argView, Menu arg0, MenuInflater arg1) {
                    lifeCycleAware.myOnCreateOptionsMenu(argView, arg0, arg1);
                }

                public boolean onOptionsItemSelected(MyView argView, MenuItem arg0) {
                    return lifeCycleAware.myOnOptionsItemSelected(argView, arg0);
                }

                public void onPause(MyView argView) {
                    lifeCycleAware.myOnPause(argView);
                }

                public void onStop(MyView argView) {
                    lifeCycleAware.myOnStop(argView);
                }

                public void onSaveInstanceState(MyView argView, Bundle arg0) {
                    lifeCycleAware.myOnSaveInstanceState(argView, arg0);
                }

                public void onDestroy(MyView argView, boolean arg0) {
                    lifeCycleAware.myOnDestroy(argView, arg0);
                }

                public void onActivityResult(MyView argView, int arg0, int arg1, Intent arg2) {
                    lifeCycleAware.myOnActivityResult(argView, arg0, arg1, arg2);
                }

                public void onViewCreated(MyView argView, Bundle arg0) {
                    lifeCycleAware.myOnViewCreated(argView, arg0);
                }

                public void onDestroyView(MyView argView) {
                    lifeCycleAware.myOnDestroyView(argView);
                }
            });
        }
        return lifeCycleAware;
    }
}
