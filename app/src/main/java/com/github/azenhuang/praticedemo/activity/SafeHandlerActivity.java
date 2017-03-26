package com.github.azenhuang.praticedemo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.github.azenhuang.praticedemo.R;
import com.github.azenhuang.praticedemo.util.MemorySafeHandler;

/**
 * Created by huangyongzheng on 3/24/17.
 */

public class SafeHandlerActivity extends AppCompatActivity {
    static final String TAG = SafeHandlerActivity.class.getSimpleName();
    private static final int DELAY_TIME = 6000;
    private static final int MESSAGE_OVERRIDE = 1;
    private static final int MESSAGE_CALLBACK = 2;

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
//        final SafeHandler handler1 = new SafeHandler();
        final MemorySafeHandler handler1 = new MemorySafeHandler();
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler1.postDelayed(runnable, DELAY_TIME);
            }
        });

//        final SafeHandler handler2 = new SafeHandler() {
        final MemorySafeHandler handler2 = new MemorySafeHandler() {
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG, "Override handleMessage done");
            }
        };

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler2.sendEmptyMessageDelayed(MESSAGE_OVERRIDE, DELAY_TIME);

            }
        });


//        final SafeHandler handler3 = new SafeHandler(new Handler.Callback() {
        final MemorySafeHandler handler3 = new MemorySafeHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Log.d(TAG, "Handler.Callback done");
                return true;
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler3.sendEmptyMessageDelayed(MESSAGE_CALLBACK, DELAY_TIME);

            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                handler3.sendEmptyMessageDelayed(1, DELAY_TIME);

            }
        });

    }
}
