package it.codingjam.lifecyclebinder.mvp;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import it.codingjam.lifecyclebinder.DefaultViewLifeCycleAware;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class Presenter extends DefaultViewLifeCycleAware<View> {

    public static final String MODEL = "MODEL";

    private View view;

    private Model model;

    private boolean loading;

    @Override
    public void onCreate(View view, Bundle bundle) {
        if (model == null) {
            if (bundle == null) {
                model = new Model();
            } else {
                bundle.getParcelable(MODEL);
            }
        }
    }

    @Override
    public void onSaveInstanceState(View view, Bundle bundle) {
        bundle.putParcelable(MODEL, model);
    }

    @Override
    public void onResume(View view) {
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
    public void onPause(View view) {
        this.view = null;
    }

    private void reloadData() {
        loading = true;
        view.showLoading();
        Observable.just(new Note("title", "description"))
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
}
