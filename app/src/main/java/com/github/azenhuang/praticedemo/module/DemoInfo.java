package com.github.azenhuang.praticedemo.module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;

/**
 * Created by huangyongzheng on 2/10/17.
 */

public class DemoInfo implements Serializable {
    public String demoName;
    public Class<? extends AppCompatActivity> targetActivity;
    public Bundle extraBundle;

    public DemoInfo(String demoName, Class<? extends AppCompatActivity> targetActivity, Bundle extraBundle) {
        this.demoName = demoName;
        this.targetActivity = targetActivity;
        this.extraBundle = extraBundle;
    }
}
