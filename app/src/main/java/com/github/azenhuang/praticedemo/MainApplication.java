package com.github.azenhuang.praticedemo;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.github.azenhuang.praticedemo.activity.NewProcessService;
import com.github.azenhuang.praticedemo.util.ProcessUtil;

/**
 * Created by huangyongzheng on 17/8/22.
 */

public class MainApplication extends Application {
    public static final String TAG = MainApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "process:" + ProcessUtil.getCurrentProcessName() + " thread:" + Thread.currentThread().toString() + " Application OnCreate");
        Log.e(TAG, "process:" + ProcessUtil.getCurrentProcessName() + " thread:" + Thread.currentThread().toString() + " before start service");
        Intent intent = new Intent(this, NewProcessService.class);
        startService(intent);
        Log.e(TAG, "process:" + ProcessUtil.getCurrentProcessName() + " thread:" + Thread.currentThread().toString() + " after start service");

    }
}
