package it.codingjam.lifecyclebinder.demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import it.codingjam.lifecyclebinder.LifeCycleAware;
import it.codingjam.lifecyclebinder.LifeCycleBinder;

public class MainActivity extends AppCompatActivity {

    @LifeCycleAware ActivityLogger activityLogger = new ActivityLogger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LifeCycleBinder.bind(this);

        findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment newFragment = fragmentManager.findFragmentByTag("newFragment");
                if (newFragment == null) {
                    fragmentManager.beginTransaction().add(R.id.container, new MyFragment(), "newFragment").commit();
                } else {
                    fragmentManager.beginTransaction().remove(newFragment).commit();
                }
            }
        });
    }
}
