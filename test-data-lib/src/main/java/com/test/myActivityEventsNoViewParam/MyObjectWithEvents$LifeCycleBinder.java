package com.test.myActivityEventsNoViewParam;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import it.codingjam.lifecyclebinder.DefaultLifeCycleAware;
import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;
import it.codingjam.lifecyclebinder.LifeCycleEvent;

public class MyObjectWithEvents$LifeCycleBinder {

    public static MyObjectWithEvents bind(LifeCycleAwareCollector collector, final MyObjectWithEvents lifeCycleAware, boolean addInList) {
        if (addInList) {
            collector.addLifeCycleAware(new DefaultLifeCycleAware<Object>() {
                public void onCreate(Object argView, Bundle arg0, Intent arg1, Bundle arg2) {
                    lifeCycleAware.myOnCreate();
                }

                public void onStart(Object argView) {
                    lifeCycleAware.myOnStart(LifeCycleEvent.START);
                }

                public void onResume(Object argView) {
                    lifeCycleAware.myOnResume();
                }

                public boolean hasOptionsMenu(Object argView) {
                    return lifeCycleAware.myHasOptionsMenu();
                }

                public void onCreateOptionsMenu(Object argView, Menu arg0, MenuInflater arg1) {
                    lifeCycleAware.myOnCreateOptionsMenu(LifeCycleEvent.CREATE_OPTION_MENU, arg0, arg1);
                }

                public boolean onOptionsItemSelected(Object argView, MenuItem arg0) {
                    lifeCycleAware.myOnOptionsItemSelected(arg0);
                    return true;
                }

                public void onPause(Object argView) {
                    lifeCycleAware.myOnPause();
                }

                public void onStop(Object argView) {
                    lifeCycleAware.myOnStop();
                }

                public void onSaveInstanceState(Object argView, Bundle arg0) {
                    lifeCycleAware.myOnSaveInstanceState(arg0);
                }

                public void onDestroy(Object argView, boolean arg0) {
                    lifeCycleAware.myOnDestroy(arg0);
                }

                public void onActivityResult(Object argView, int arg0, int arg1, Intent arg2) {
                    lifeCycleAware.myOnActivityResult(arg0, arg1, arg2);
                }

                public void onViewCreated(Object argView, Bundle arg0) {
                    lifeCycleAware.myOnViewCreated(arg0);
                }

                public void onDestroyView(Object argView) {
                    lifeCycleAware.myOnDestroyView();
                }
            });
        }
        return lifeCycleAware;
    }
}
