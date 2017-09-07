package com.github.azenhuang.praticedemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.github.azenhuang.praticedemo.R;

/**
 * Created by huangyongzheng on 17/6/21.
 */

public class SecondaryTextView extends AppCompatTextView {
    private int primaryTextSize;
    private int secondaryTextSize;
    private int secondaryTriggerLine;

    public SecondaryTextView(Context context) {
        this(context, null);
    }

    public SecondaryTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SecondaryTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        primaryTextSize = (int) getTextSize();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SecondaryTextView, defStyleAttr, 0);
        secondaryTextSize = typedArray.getDimensionPixelSize(R.styleable.SecondaryTextView_secondaryTextSize, primaryTextSize);
        secondaryTextSize = Math.min(primaryTextSize, secondaryTextSize);
        secondaryTriggerLine = typedArray.getInteger(R.styleable.SecondaryTextView_secondaryTriggerLine, 2);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (primaryTextSize == secondaryTextSize) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        setTextSize(TypedValue.COMPLEX_UNIT_PX, primaryTextSize);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getLineCount() < secondaryTriggerLine) {
            return;
        }

        setTextSize(TypedValue.COMPLEX_UNIT_PX, secondaryTextSize);
        // Re-measure with new setup
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
