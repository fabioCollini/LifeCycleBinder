package it.codingjam.lifecyclebinder.mvvm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import it.codingjam.lifecyclebinder.DefaultLifeCycleAware;
import it.codingjam.lifecyclebinder.LifeCycleBinder;
import it.codingjam.lifecyclebinder.mvp.R;

public class Navigator extends DefaultLifeCycleAware<FragmentActivity> {

    private FragmentActivity activity;

    @Override public void onCreate(FragmentActivity activity, Bundle savedInstanceState, Intent intent, Bundle arguments) {
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
