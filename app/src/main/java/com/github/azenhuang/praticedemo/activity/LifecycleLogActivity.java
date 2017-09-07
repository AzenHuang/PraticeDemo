package com.github.azenhuang.praticedemo.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by huangyongzheng on 17/4/24.
 */

public class LifecycleLogActivity extends AppCompatActivity {
    private static final String TAG = "LifecycleLog";

    @Override
    public void onAttachedToWindow() {
        Log.d(TAG, this + ">>>onAttachedToWindow");
        super.onAttachedToWindow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, this + ">>>onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, this + ">>>onRestart");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, this + ">>>onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, this + ">>>onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, this + ">>>onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, this + ">>>onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, this + ">>>onDestroy");
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        Log.d(TAG, this + ">>>onSaveInstanceState");
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, this + ">>>onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, this + ">>>onNewIntent");
        super.onNewIntent(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, this + ">>>onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }
}
