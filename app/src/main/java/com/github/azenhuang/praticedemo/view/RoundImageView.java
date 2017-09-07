package com.github.azenhuang.praticedemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.github.azenhuang.praticedemo.R;

/**
 * 圆角ImageView
 */
public class RoundImageView extends AppCompatImageView {
    /**
     * 圆角大小的默认值
     */
    private static final int BORDER_RADIUS_DEFAULT = 2;

    /**
     * 绘图的Paint
     */
    private Paint bitmapPaint;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    /**
     * 圆角的半径
     */
    private int rectRadius;
    private RectF mRect = new RectF();
    private Path mPath = new Path();

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        rectRadius = a.getDimensionPixelSize(R.styleable.RoundImageView_borderRadius,
                dp2px(BORDER_RADIUS_DEFAULT));
        a.recycle();

        bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(true);
        bitmapPaint.setFilterBitmap(true);
        bitmapCanvas = new Canvas();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if ((w != oldh || h != oldh)
                && (w != 0 && h != 0)) {
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmapCanvas.setBitmap(bitmap);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }

        mPath.reset();
        mRect.set(0, 0, getWidth(), getHeight());
        mPath.addRoundRect(mRect, rectRadius, rectRadius, Path.Direction.CW);

        int count = bitmapCanvas.save();
        bitmapCanvas.clipPath(mPath);
        super.onDraw(bitmapCanvas);
        bitmapCanvas.restoreToCount(count);

        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
    }

    private static final String STATE_INSTANCE = "state_instance";
    private static final String STATE_BORDER_RADIUS = "state_border_radius";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(STATE_BORDER_RADIUS, rectRadius);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(((Bundle) state)
                    .getParcelable(STATE_INSTANCE));
            this.rectRadius = bundle.getInt(STATE_BORDER_RADIUS);
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    public void setBorderRadius(int borderRadius) {
        int pxVal = dp2px(borderRadius);
        if (this.rectRadius != pxVal) {
            this.rectRadius = pxVal;
            invalidate();
        }
    }

    public int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

}
