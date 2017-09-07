package com.github.azenhuang.praticedemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by huangyongzheng on 17/6/18.
 */

public class LifecycleLogView extends View {
    public static final String TAG = LifecycleLogView.class.getSimpleName();

    public LifecycleLogView(Context context) {
        super(context);
        log("LifecycleLogView(Context context)");
    }

    public LifecycleLogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        log("LifecycleLogView(Context context, AttributeSet attrs)");
    }

    public LifecycleLogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        log("LifecycleLogView(Context context, AttributeSet attrs, int defStyleAttr)");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LifecycleLogView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        log("LifecycleLogView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        log("onFinishInflate");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        log("onAttachedToWindow");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        log("onDetachedFromWindow");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        log("onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        log("onLayout");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        log("onSizeChanged");
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        log("dispatchDraw");
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        log("onDraw");
    }

    private void log(String msg) {
        Log.d(TAG, msg);
    }
}
