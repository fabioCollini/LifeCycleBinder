package com.test.myActivityEventsViewParam;

import android.content.Intent;
import android.os.Bundle;
import com.test.MyView;
import it.codingjam.lifecyclebinder.DefaultLifeCycleAware;
import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;

public class MyObjectWithEvents$LifeCycleBinder {

    public static MyObjectWithEvents bind(LifeCycleAwareCollector collector, final MyObjectWithEvents lifeCycleAware, boolean addInList) {
        if (addInList) {
            collector.addLifeCycleAware(new DefaultLifeCycleAware<MyView>() {
                public void onCreate(MyView argView, Bundle arg0, Intent arg1, Bundle arg2) {
                    lifeCycleAware.myOnCreate(argView);
                }
            });
        }
        return lifeCycleAware;
    }
}
