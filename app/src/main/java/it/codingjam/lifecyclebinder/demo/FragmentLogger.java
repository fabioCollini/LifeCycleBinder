package it.codingjam.lifecyclebinder.demo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import it.codingjam.lifecyclebinder.DefaultViewLifeCycleAware;


public class FragmentLogger extends DefaultViewLifeCycleAware<Fragment> {

    public static final String TAG = "ACTIVITY_LOG";

    private String name;

    private static int sequence = 1;

    public FragmentLogger(String name) {
        this.name = (sequence++) + "_" + name;
    }

    @Override
    public void onCreate(Fragment activity, Bundle bundle) {
        Log.i(TAG, name + " Creating activity:" + activity);
    }

    @Override
    public void onStart(Fragment activity) {
//        Log.i(TAG, name + " Starting activity:" + activity);
    }

    @Override
    public void onResume(Fragment activity) {
        Log.i(TAG, name + " Resuming activity:" + activity);
    }

    @Override
    public void onPause(Fragment activity) {
        Log.i(TAG, name + " Pausing activity:" + activity);
    }

    @Override
    public void onStop(Fragment activity) {
//        Log.i(TAG, name + " Stopping activity:" + activity);
    }

    @Override
    public void onDestroy(Fragment activity) {
        Log.i(TAG, name + " Destroying activity:" + activity);
    }
}
