package it.codingjam.lifecyclebinder.mvvm;

import android.databinding.BindingAdapter;
import android.view.View;

public class DataBindingAdapters {

    @BindingAdapter("visible")
    public static void bindGone(View view, Boolean visible) {
        view.setVisibility(visible != null && visible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("android:onClick")
    public static void bindOnClick(View view, final Runnable runnable) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
    }
}
