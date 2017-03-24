package com.github.azenhuang.praticedemo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.github.azenhuang.praticedemo.R;

/**
 * Created by huangyongzheng on 3/24/17.
 */

public class HandlerActivity extends AppCompatActivity {
    static final String TAG = HandlerActivity.class.getSimpleName();
    private static final int DELAY_TIME = 8000000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_handler_demo);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run over");
            }
        };
        final Handler handler1 = new Handler();
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler1.postDelayed(runnable, DELAY_TIME);
            }
        });

        final Handler handler2 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG, "Override handleMessage done");
            }
        };

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler2.sendEmptyMessageDelayed(1, DELAY_TIME);

            }
        });


        final Handler handler3 = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Log.d(TAG, "Handler.Callback done");
                return true;
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler3.sendEmptyMessageDelayed(1, DELAY_TIME);

            }
        });



    }
}
