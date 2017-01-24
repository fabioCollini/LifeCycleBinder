package com.test.myActivityEventsViewParam;

import android.content.Intent;
import android.os.Bundle;
import com.test.MyView;
import it.codingjam.lifecyclebinder.DefaultLifeCycleAware;
import it.codingjam.lifecyclebinder.LifeCycleAwareCollector;

public class MyObjectWithEvents$LifeCycleBinder {

    public static void bind(LifeCycleAwareCollector collector, final MyObjectWithEvents view) {
        collector.addLifeCycleAware(new DefaultLifeCycleAware<MyView>() {
            public void onCreate(MyView argView, Bundle arg0, Intent arg1, Bundle arg2) {
                view.myOnCreate(argView);
            }
        });
    }
}
