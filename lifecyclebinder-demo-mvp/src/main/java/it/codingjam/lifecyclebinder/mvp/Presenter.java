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

package it.codingjam.lifecyclebinder.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import it.codingjam.lifecyclebinder.BindLifeCycle;
import it.codingjam.lifecyclebinder.DefaultLifeCycleAware;
import it.codingjam.lifecyclebinder.LifeCycleAware;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class Presenter extends DefaultLifeCycleAware<MvpView> implements LifeCycleAware<MvpView> {

    private static final int SHARE_REQUEST_CODE = 123;

    private static final String MODEL = "MODEL";

    private MvpView view;

    private Model model;

    @BindLifeCycle
    Logger logger = new Logger();

    private boolean loading;

    @Override
    public void onCreate(MvpView view, Bundle savedInstanceState, Intent intent, Bundle arguments) {
        if (model == null) {
            if (savedInstanceState != null) {
                model = savedInstanceState.getParcelable(MODEL);
            }
            if (model == null) {
                model = new Model();
            }
        }
    }

    @Override
    public void onDestroy(MvpView view, boolean changingConfigurations) {
        super.onDestroy(view, changingConfigurations);
    }

    @Override
    public void onSaveInstanceState(MvpView view, Bundle bundle) {
        bundle.putParcelable(MODEL, model);
    }

    @Override
    public void onResume(MvpView view) {
        this.view = view;
        if (loading) {
            view.showLoading();
        } else {
            if (model.getNote() == null) {
                reloadData();
            } else {
                view.update(model);
            }
        }
    }

    @Override
    public void onPause(MvpView view) {
        this.view = null;
    }

    private void reloadData() {
        loading = true;
        view.showLoading();
        Observable.just(new Note("title" + new Random().nextInt(10), "description"))
                .delay(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Note>() {
                    @Override
                    public void call(Note note) {
                        loading = false;
                        model.setNote(note);
                        if (view != null) {
                            view.update(model);
                        }
                    }
                });
    }

    public void share() {
        view.share(model.getNote().getTitle(), SHARE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(MvpView view, int requestCode, int resultCode, Intent data) {
        if (requestCode == SHARE_REQUEST_CODE) {
            //...
        }
    }

    @Override
    public boolean hasOptionsMenu(MvpView view) {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(MvpView view, Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.share_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MvpView view, MenuItem item) {
        if (item.getItemId() == R.id.share_item) {
            share();
            return true;
        }
        return super.onOptionsItemSelected(view, item);
    }
}
