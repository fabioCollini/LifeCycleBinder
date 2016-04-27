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
