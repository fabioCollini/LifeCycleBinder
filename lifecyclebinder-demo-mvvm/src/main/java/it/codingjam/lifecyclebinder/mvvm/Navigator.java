package it.codingjam.lifecyclebinder.mvvm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import it.codingjam.lifecyclebinder.BindEvent;
import it.codingjam.lifecyclebinder.LifeCycleBinder;
import it.codingjam.lifecyclebinder.mvp.R;

import static it.codingjam.lifecyclebinder.LifeCycleEvent.CREATE;

public class Navigator {

    private FragmentActivity activity;

    @BindEvent(CREATE) public void onCreate(FragmentActivity activity, Bundle arg0, Intent arg1, Bundle arg2) {
        this.activity = activity;
    }

    public void share(String message, int requestCode) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        Intent chooser = Intent.createChooser(sendIntent, activity.getResources().getText(R.string.share));
        LifeCycleBinder.startActivityForResult(activity, chooser, requestCode);
    }
}
