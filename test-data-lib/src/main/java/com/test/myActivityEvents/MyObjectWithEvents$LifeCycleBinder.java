package com.test.myActivityEvents;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.test.MyView;
import it.codingjam.lifecyclebinder.DefaultLifeCycleAware;
import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;

public class MyObjectWithEvents$LifeCycleBinder {

    public static void bind(LifeCycleAwareCollector<? extends MyView> collector, final MyObjectWithEvents view) {
        collector.addLifeCycleAware(new DefaultLifeCycleAware<MyView>() {
            public void onCreate(MyView argView, Bundle arg0, Intent arg1, Bundle arg2) {
                view.myOnCreate(argView, arg0, arg1, arg2);
            }

            public void onStart(MyView argView) {
                view.myOnStart(argView);
            }

            public void onResume(MyView argView) {
                view.myOnResume(argView);
            }

            public boolean hasOptionsMenu(MyView argView) {
                return view.myHasOptionsMenu(argView);
            }

            public void onCreateOptionsMenu(MyView argView, Menu arg0, MenuInflater arg1) {
                view.myOnCreateOptionsMenu(argView, arg0, arg1);
            }

            public boolean onOptionsItemSelected(MyView argView, MenuItem arg0) {
                return view.myOnOptionsItemSelected(argView, arg0);
            }

            public void onPause(MyView argView) {
                view.myOnPause(argView);
            }

            public void onStop(MyView argView) {
                view.myOnStop(argView);
            }

            public void onSaveInstanceState(MyView argView, Bundle arg0) {
                view.myOnSaveInstanceState(argView, arg0);
            }

            public void onDestroy(MyView argView, boolean arg0) {
                view.myOnDestroy(argView, arg0);
            }

            public void onActivityResult(MyView argView, int arg0, int arg1, Intent arg2) {
                view.myOnActivityResult(argView, arg0, arg1, arg2);
            }

            public void onViewCreated(MyView argView, Bundle arg0) {
                view.myOnViewCreated(argView, arg0);
            }

            public void onDestroyView(MyView argView) {
                view.myOnDestroyView(argView);
            }
        });
    }
}
