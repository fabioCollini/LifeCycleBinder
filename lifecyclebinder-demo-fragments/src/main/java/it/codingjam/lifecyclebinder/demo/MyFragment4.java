package it.codingjam.lifecyclebinder.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.codingjam.lifecyclebinder.LifeCycleAware;
import it.codingjam.lifecyclebinder.LifeCycleBinder;

public class MyFragment4 extends Fragment {
    @LifeCycleAware FragmentLogger fragmentLogger = new FragmentLogger("MyFragment4");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LifeCycleBinder.bind(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        ((TextView) view.findViewById(R.id.hello)).setText("MyFragment4");
//        view.findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fragmentManager = getChildFragmentManager();
//                Fragment newFragment = fragmentManager.findFragmentByTag("newFragment");
//                if (newFragment == null) {
//                    fragmentManager.beginTransaction().add(R.id.container, new MyFragment4(), "newFragment").commit();
//                } else {
//                    fragmentManager.beginTransaction().remove(newFragment).commit();
//                }
//            }
//        });

        return view;
    }

}
