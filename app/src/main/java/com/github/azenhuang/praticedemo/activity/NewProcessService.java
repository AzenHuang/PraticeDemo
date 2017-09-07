package com.github.azenhuang.praticedemo.activity;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.azenhuang.praticedemo.util.ProcessUtil;

/**
 * Created by huangyongzheng on 17/7/18.
 */

public class NewProcessService extends Service {

    private static final String TAG = NewProcessService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "process:" + ProcessUtil.getCurrentProcessName() + " thread:" + Thread.currentThread().toString() + " NewProcessService OnCreate");
        Log.e(TAG, "process:" + ProcessUtil.getCurrentProcessName() + " thread:" + Thread.currentThread().toString() + " NewProcessService before sleep");
        try {
            Thread.currentThread().sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            Log.e(TAG, "process:" + ProcessUtil.getCurrentProcessName() + " thread:" + Thread.currentThread().toString() + " NewProcessService after sleep");

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
