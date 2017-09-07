package com.github.azenhuang.praticedemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.azenhuang.praticedemo.R;

/**
 * Created by huangyongzheng on 17/7/17.
 */

public class CustomTitleBar extends RelativeLayout {
    private ImageView leftBar;
    private ImageView rightBar;
    private TextView titleBar;

    private Drawable leftDrawable;
    private Drawable rightDrawable;
    private CharSequence titleText;
    private int titleTextColor;
    private int titleTextSize;
    private BarClickListener barClickListener;

    public interface BarClickListener {
        void onLeftBarClick(ImageView leftBar);

        void onRightBarClick(ImageView rightBar);

        void onTitleBarClick(TextView titleBar);
    }

    public CustomTitleBar(Context context) {
        this(context, null);
    }

    public CustomTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initView();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTitleBar);
        leftDrawable = typedArray.getDrawable(R.styleable.CustomTitleBar_left_drawable);
        rightDrawable = typedArray.getDrawable(R.styleable.CustomTitleBar_right_drawable);
        titleText = typedArray.getString(R.styleable.CustomTitleBar_title_text);
        titleTextSize = typedArray.getDimensionPixelSize(R.styleable.CustomTitleBar_title_text_size, dp2px(18));
        titleTextColor = typedArray.getColor(R.styleable.CustomTitleBar_title_text_color, Color.WHITE);
        typedArray.recycle();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.custom_title_bar, this, true);
        titleBar = (TextView) findViewById(R.id.title_bar_title);
        leftBar = (ImageView) findViewById(R.id.title_bar_left);
        rightBar = (ImageView) findViewById(R.id.title_bar_right);

        titleBar.setText(titleText);
        titleBar.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        titleBar.setTextColor(titleTextColor);
        leftBar.setImageDrawable(leftDrawable);
        rightBar.setImageDrawable(rightDrawable);

        titleBar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (barClickListener != null) {
                    barClickListener.onTitleBarClick(titleBar);
                }
            }
        });
        leftBar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (barClickListener != null) {
                    barClickListener.onLeftBarClick(leftBar);
                }
            }
        });
        rightBar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (barClickListener != null) {
                    barClickListener.onRightBarClick(rightBar);
                }
            }
        });
    }

    public void setLeftDrawable(Drawable leftDrawable) {
        this.leftDrawable = leftDrawable;
        leftBar.setImageDrawable(leftDrawable);
    }

    public void setRightDrawable(Drawable rightDrawable) {
        this.rightDrawable = rightDrawable;
        rightBar.setImageDrawable(rightDrawable);
    }

    public void setTitleText(CharSequence titleText) {
        this.titleText = titleText;
        titleBar.setText(titleText);
    }

    public void setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
        titleBar.setTextColor(titleTextColor);
    }

    public void setTitleTextSize(int titleTextSize) {
        this.titleTextSize = titleTextSize;
        titleBar.setTextSize(titleTextSize);
    }

    public void setBarClickListener(BarClickListener barClickListener) {
        this.barClickListener = barClickListener;
    }

    public ImageView getLeftBar() {
        return leftBar;
    }

    public ImageView getRightBar() {
        return rightBar;
    }

    public TextView getTitleBar() {
        return titleBar;
    }

    private int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}
