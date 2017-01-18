package com.test.myActivityEventsNoViewParam;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import it.codingjam.lifecyclebinder.DefaultLifeCycleAware;
import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;

public class MyObjectWithEvents$LifeCycleBinder {

    public static void bind(LifeCycleAwareCollector<? extends Object> collector, final MyObjectWithEvents view) {
        collector.addLifeCycleAware(new DefaultLifeCycleAware<Object>() {
            public void onCreate(Object argView, Bundle arg0, Intent arg1, Bundle arg2) {
                view.myOnCreate(arg0, arg1, arg2);
            }

            public void onStart(Object argView) {
                view.myOnStart();
            }

            public void onResume(Object argView) {
                view.myOnResume();
            }

            public boolean hasOptionsMenu(Object argView) {
                return view.myHasOptionsMenu();
            }

            public void onCreateOptionsMenu(Object argView, Menu arg0, MenuInflater arg1) {
                view.myOnCreateOptionsMenu(arg0, arg1);
            }

            public boolean onOptionsItemSelected(Object argView, MenuItem arg0) {
                return view.myOnOptionsItemSelected(arg0);
            }

            public void onPause(Object argView) {
                view.myOnPause();
            }

            public void onStop(Object argView) {
                view.myOnStop();
            }

            public void onSaveInstanceState(Object argView, Bundle arg0) {
                view.myOnSaveInstanceState(arg0);
            }

            public void onDestroy(Object argView, boolean arg0) {
                view.myOnDestroy(arg0);
            }

            public void onActivityResult(Object argView, int arg0, int arg1, Intent arg2) {
                view.myOnActivityResult(arg0, arg1, arg2);
            }

            public void onViewCreated(Object argView, Bundle arg0) {
                view.myOnViewCreated(arg0);
            }

            public void onDestroyView(Object argView) {
                view.myOnDestroyView();
            }
        });
    }
}
