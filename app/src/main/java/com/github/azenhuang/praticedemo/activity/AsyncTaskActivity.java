package com.github.azenhuang.praticedemo.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.github.azenhuang.praticedemo.R;
import com.github.azenhuang.praticedemo.util.MemorySafeAsyncTask;
import com.github.azenhuang.praticedemo.util.SafeAsyncTask;

/**
 * Created by huangyongzheng on 3/24/17.
 */

public class AsyncTaskActivity extends AppCompatActivity {
    static final String TAG = AsyncTaskActivity.class.getSimpleName();
    private static final int SLEEP_TIME = 5000;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_async_task_demo);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyAsyncTask().execute((Void) null);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new MySafeAsyncTask().execute((Void) null);
                new MyMemorySafeAsyncTask().execute((Void) null);
            }
        });
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new MySafeAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
                new MyMemorySafeAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
            }
        });

    }

    private class MyAsyncTask extends AsyncTask<Void, Integer, Void> {
        int progress = 0;

        @Override
        protected Void doInBackground(Void... params) {
            publishProgress(progress);
            try {
                while (progress <= 100) {
                    Thread.sleep(SLEEP_TIME);
                    progress += 10;
                    publishProgress(progress);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
            Log.d(TAG, this.toString() + ">>>" + progress);
        }
    }

    private class MySafeAsyncTask extends SafeAsyncTask<Void, Integer, Void> {
        int progress = 0;

        @Override
        protected Void doInBackground(Void... params) {
            publishProgress(progress);
            try {
                while (progress <= 100) {
                    Thread.sleep(SLEEP_TIME);
                    progress += 10;
                    publishProgress(progress);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
            Log.d(TAG, this.toString() + ">>>" + progress);
        }
    }

    private class MyMemorySafeAsyncTask extends MemorySafeAsyncTask<Void, Integer, Void> {
        int progress = 0;

        @Override
        protected Void doInBackground(Void... params) {
            publishProgress(progress);
            try {
                while (progress <= 100) {
                    Thread.sleep(SLEEP_TIME);
                    progress += 10;
                    publishProgress(progress);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
            Log.d(TAG, this.toString() + ">>>" + progress);
        }
    }
}
