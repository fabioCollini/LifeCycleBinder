package it.codingjam.lifecyclebinder.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.concurrent.Callable;

import it.codingjam.lifecyclebinder.LifeCycleAware;
import it.codingjam.lifecyclebinder.LifeCycleBinder;

public class MyFragment extends Fragment {
    @LifeCycleAware(retained = true, name = "myName") Callable<FragmentLogger> fragmentLogger = new Callable<FragmentLogger>() {
        @Override
        public FragmentLogger call() throws Exception {
            return new FragmentLogger("MyFragment");
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LifeCycleBinder.bind(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        ((TextView) view.findViewById(R.id.hello)).setText("MyFragment");
        view.findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getChildFragmentManager();
                Fragment newFragment = fragmentManager.findFragmentByTag("newFragment");
                if (newFragment == null) {
                    fragmentManager.beginTransaction().add(R.id.container, new MyFragment2(), "newFragment").commit();
                } else {
                    fragmentManager.beginTransaction().remove(newFragment).commit();
                }
            }
        });

        return view;
    }
}
