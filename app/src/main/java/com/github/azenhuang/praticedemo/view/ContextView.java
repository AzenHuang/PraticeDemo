package com.github.azenhuang.praticedemo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by huangyongzheng on 17/7/18.
 */

public class ContextView extends View {
    public static final String TAG = ContextView.class.getSimpleName();

    public ContextView(Context context) {
        this(context, null);
    }

    public ContextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.e(TAG, "view context:" + context.getClass().getName());
//        Log.e(TAG, "view context:"+context.get.getClass().getName());
    }
}
