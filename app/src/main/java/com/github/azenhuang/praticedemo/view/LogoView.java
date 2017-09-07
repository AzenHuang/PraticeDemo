package com.github.azenhuang.praticedemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.github.azenhuang.praticedemo.R;

/**
 * Created by huangyongzheng on 17/7/10.
 */

public class LogoView extends View {
    private static final int DEFAULT_SIZE_DP = 48;
    private static final int DEFAULT_TEXT_SIZE_SP = 14;
    private static final String DEFAULT_LOGO_TEXT = "美团";
    private static final int DEFAULT_CORNER_RADIUS_DP = 4;
    private static final float DEFAULT_CIRCLE_RADIO = 0.85F;
    private static final int DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private static final int DEFAULT_CIRCLE_COLOR = Color.parseColor("#06C1AE");
    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;

    private int cornerRadius;
    private String logoText;
    private int textColor;
    private int backgroundColor;
    private int circleColor;

    private final int defaultSize;
    private float textSize;
    private Paint paint;
    private RectF mRect = new RectF();
    private Path mPath = new Path();

    public LogoView(Context context) {
        this(context, null);
    }

    public LogoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LogoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LogoView);
        cornerRadius = a.getDimensionPixelSize(R.styleable.LogoView_cornerRadius,
                getDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CORNER_RADIUS_DP));
        if (a.hasValue(R.styleable.LogoView_logoText)) {
            logoText = a.getString(R.styleable.LogoView_logoText);
        } else {
            logoText = DEFAULT_LOGO_TEXT;
        }
        textColor = a.getColor(R.styleable.LogoView_textColor, DEFAULT_TEXT_COLOR);
        backgroundColor = a.getColor(R.styleable.LogoView_backgroundColor, DEFAULT_BACKGROUND_COLOR);
        circleColor = a.getColor(R.styleable.LogoView_circleColor, DEFAULT_CIRCLE_COLOR);
        a.recycle();

        defaultSize = getDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_SIZE_DP);
        textSize = getDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE_SP);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int horizontalPadding = getPaddingLeft() + getPaddingRight();
        int verticalPadding = getPaddingTop() + getPaddingBottom();
        //注意计算padding
        int desiredWidth = defaultSize + horizontalPadding;
        int desiredHeight = defaultSize + verticalPadding;
        setMeasuredDimension(resolveSize(desiredWidth, widthMeasureSpec),
                resolveSize(desiredHeight, heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int dividedPaddingWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int dividedPaddingHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        int delta = (dividedPaddingWidth - dividedPaddingHeight) / 2;
        //实际绘制的Logo大小
        int size;
        //Canvas移动的X轴距离
        int translateX = getPaddingLeft();
        //Canvas移动的Y轴距离
        int translateY = getPaddingTop();
        //计算画布需要移动的距离，及Logo绘制大小
        if (delta > 0) {
            size = dividedPaddingHeight;
            translateX += delta;
        } else {
            size = dividedPaddingWidth;
            translateY -= delta;
        }
        canvas.save();
        canvas.translate(translateX, translateY);

        //画圆角矩形背景
        canvas.save();
        mPath.reset();
        mRect.set(0, 0, size, size);
        mPath.addRoundRect(mRect, cornerRadius, cornerRadius, Path.Direction.CW);
        canvas.clipPath(mPath);
        canvas.drawColor(backgroundColor);
        canvas.restore();

        //画圆
        paint.setColor(circleColor);
        float circleRadius = size / 2 * DEFAULT_CIRCLE_RADIO;
        canvas.drawCircle(size / 2, size / 2, circleRadius, paint);

        //画文字
        textSize = circleRadius * 5 / 6;
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        if (!TextUtils.isEmpty(logoText)) {
            if (logoText.length() < 2) {
                canvas.drawText(logoText, (size - textSize) / 2, size / 2 + textSize / 3, paint);
            } else {
                canvas.drawText(logoText, 0, 2, size / 2 - textSize, size / 2 + textSize / 3, paint);
            }
        }

        canvas.restore();
    }

    private int getDimension(int unit, int dp) {
        return (int) TypedValue.applyDimension(unit, dp, getResources().getDisplayMetrics());
    }
}
