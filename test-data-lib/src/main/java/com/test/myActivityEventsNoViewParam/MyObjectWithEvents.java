package com.test.myActivityEventsNoViewParam;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import it.codingjam.lifecyclebinder.BindEvent;
import it.codingjam.lifecyclebinder.LifeCycleEvent;

import static it.codingjam.lifecyclebinder.LifeCycleEvent.ACTIVITY_RESULT;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.CREATE;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.CREATE_OPTION_MENU;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.DESTROY;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.DESTROY_VIEW;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.HAS_OPTION_MENU;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.OPTION_ITEM_SELECTED;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.PAUSE;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.RESUME;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.SAVE_INSTANCE_STATE;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.START;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.STOP;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.VIEW_CREATED;

public class MyObjectWithEvents {
    @BindEvent(CREATE) public void myOnCreate() {
    }

    @BindEvent(START) public void myOnStart(LifeCycleEvent event) {
    }

    @BindEvent(RESUME) public void myOnResume() {
    }

    @BindEvent(HAS_OPTION_MENU) public boolean myHasOptionsMenu() {
        return false;
    }

    @BindEvent(CREATE_OPTION_MENU) public void myOnCreateOptionsMenu(LifeCycleEvent event, Menu menu, MenuInflater inflater) {
    }

    @BindEvent(OPTION_ITEM_SELECTED) public void myOnOptionsItemSelected(MenuItem item) {
    }

    @BindEvent(PAUSE) public void myOnPause() {
    }

    @BindEvent(STOP) public void myOnStop() {
    }

    @BindEvent(SAVE_INSTANCE_STATE) public void myOnSaveInstanceState(Bundle bundle) {
    }

    @BindEvent(DESTROY) public void myOnDestroy(boolean changingConfigurations) {
    }

    @BindEvent(ACTIVITY_RESULT) public void myOnActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @BindEvent(VIEW_CREATED) public void myOnViewCreated(Bundle savedInstanceState) {
    }

    @BindEvent(DESTROY_VIEW) public void myOnDestroyView() {
    }
}
