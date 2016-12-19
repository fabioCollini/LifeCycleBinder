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
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.Callable;

import it.codingjam.lifecyclebinder.BindLifeCycle;
import it.codingjam.lifecyclebinder.LifeCycleBinder;
import it.codingjam.lifecyclebinder.RetainedObjectProvider;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements MvpView {

    private ProgressBar progress;
    private TextView title;
    private TextView description;

    @BindLifeCycle
    Logger logger = new Logger();

    @RetainedObjectProvider("presenter")
    Callable<Presenter> presenterFactory = new Callable<Presenter>() {
        @Override
        public Presenter call() throws Exception {
            return new Presenter();
        }
    };

    Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = (ProgressBar) findViewById(R.id.progress);
        title = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);

        LifeCycleBinder.bind(this);

        findViewById(R.id.share).setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                presenter.share();
            }
        });
    }

    @Override
    public void update(Model model) {
        progress.setVisibility(GONE);
        title.setText(model.getNote().getTitle());
        description.setText(model.getNote().getDescription());
    }

    @Override
    public void showLoading() {
        progress.setVisibility(VISIBLE);
    }

    @Override
    public void share(String message, int requestCode) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        LifeCycleBinder.startActivityForResult(this, Intent.createChooser(sendIntent, getResources().getText(R.string.share)), requestCode);
    }
}
