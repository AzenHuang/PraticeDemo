package com.github.azenhuang.praticedemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

import com.github.azenhuang.praticedemo.R;

/**
 * 对于一个属性可以在多个地方指定它的值，如XML直接定义，style，Theme，而这些位置定义的值有一个优先级，按优先级从高到低依次是：
 * XML中>style定义>Theme中的defStyleAttr引用值或defStyleRes指定的默认值>Theme中指定
 */
public class CustomTextView extends AppCompatTextView {
    
    private static final String TAG = CustomTextView.class.getSimpleName();

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.CustomizeDefStyleAttr);
//        this(context, attrs, 0);

    }
    
    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomizeTextView, defStyle,
                R.style.CustomizeDefStyleRes);
        String one = a.getString(R.styleable.CustomizeTextView_attr_one);
        String two = a.getString(R.styleable.CustomizeTextView_attr_two);
        String three = a.getString(R.styleable.CustomizeTextView_attr_three);
        String four = a.getString(R.styleable.CustomizeTextView_attr_four);
        Log.w(TAG, "one:" + one);
        Log.w(TAG, "two:" + two);
        Log.w(TAG, "three:" + three);
        Log.w(TAG, "four:" + four);

        append("\none:" + one);
        append("\ntwo:" + two);
        append("\nthree:" + three);
        append("\nfour:" + four);
        a.recycle();

    }
}
