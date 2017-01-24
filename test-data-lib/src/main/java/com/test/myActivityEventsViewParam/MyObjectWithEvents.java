package com.test.myActivityEventsViewParam;

import com.test.MyView;
import it.codingjam.lifecyclebinder.BindEvent;

import static it.codingjam.lifecyclebinder.LifeCycleEvent.CREATE;

public class MyObjectWithEvents {
    @BindEvent(CREATE) public void myOnCreate(MyView myView) {
    }
}
