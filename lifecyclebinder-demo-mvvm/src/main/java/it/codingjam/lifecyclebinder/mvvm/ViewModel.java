/*
 *   Copyright 2016 Fabio Collini.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package it.codingjam.lifecyclebinder.mvvm;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import it.codingjam.lifecyclebinder.BindEvent;
import it.codingjam.lifecyclebinder.BindLifeCycle;
import it.codingjam.lifecyclebinder.mvp.R;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static it.codingjam.lifecyclebinder.LifeCycleEvent.ACTIVITY_RESULT;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.CREATE;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.CREATE_OPTION_MENU;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.HAS_OPTION_MENU;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.OPTION_ITEM_SELECTED;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.RESUME;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.SAVE_INSTANCE_STATE;

public class ViewModel {

    private static final int SHARE_REQUEST_CODE = 123;

    private static final String MODEL = "MODEL";

    private Model model;

    //@BindLifeCycle Logger logger = new Logger();

    @BindLifeCycle Navigator navigator = new Navigator();

    public final ObservableBoolean loading = new ObservableBoolean();

    @BindEvent(CREATE)
    public void onCreate(MainActivity view, Bundle savedInstanceState, Intent intent, Bundle arguments) {
        if (model == null) {
            if (savedInstanceState != null) {
                model = savedInstanceState.getParcelable(MODEL);
            }
            if (model == null) {
                model = new Model();
            }
        }
    }

    @BindEvent(SAVE_INSTANCE_STATE)
    public void onSaveInstanceState(MainActivity view, Bundle bundle) {
        bundle.putParcelable(MODEL, model);
    }

    @BindEvent(RESUME)
    public void onResume(MainActivity view) {
        if (!loading.get() && model.note.get() == null) {
            reloadData();
        }
    }

    private void reloadData() {
        loading.set(true);
        Observable.just(new Note("title" + new Random().nextInt(10), "description"))
                .delay(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Note>() {
                    @Override
                    public void call(Note note) {
                        loading.set(false);
                        model.note.set(note);
                    }
                });
    }

    public void share() {
        navigator.share(model.note.get().getTitle(), SHARE_REQUEST_CODE);
    }

    @BindEvent(ACTIVITY_RESULT)
    public void onActivityResult(MainActivity view, int requestCode, int resultCode, Intent data) {
        if (requestCode == SHARE_REQUEST_CODE) {
            //...
        }
    }

    @BindEvent(HAS_OPTION_MENU)
    public boolean hasOptionsMenu(MainActivity view) {
        return true;
    }

    @BindEvent(CREATE_OPTION_MENU)
    public void onCreateOptionsMenu(MainActivity view, Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.share_menu, menu);
    }

    @BindEvent(OPTION_ITEM_SELECTED)
    public boolean onOptionsItemSelected(MainActivity view, MenuItem item) {
        if (item.getItemId() == R.id.share_item) {
            share();
            return true;
        }
        return false;
    }

    public Model getModel() {
        return model;
    }
}
