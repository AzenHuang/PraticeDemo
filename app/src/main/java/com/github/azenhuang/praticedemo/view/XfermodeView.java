package com.github.azenhuang.praticedemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.github.azenhuang.praticedemo.R;


public class XfermodeView extends View {

    Paint paint;
    float cellSize = 0;
    float cellHorizontalOffset = 0;
    float cellVerticalOffset = 0;
    float circleRadius = 0;
    float rectSize = 0;
//    int circleColor = 0x77ffcc44;//黄色
//    int rectColor = 0x7766aaff;//蓝色
    float textSize = getResources().getDimensionPixelSize(R.dimen.textSize);

    RectF colorModesRect = new RectF();
    int colorModeIndex = 0;
    private static final int[] circleColors = {0xffffcc44, 0xffffcc44, 0x77ffcc44, 0x77ffcc44};
    private static final int[] rectColors = {0xff66aaff, 0x7766aaff, 0xff66aaff, 0x7766aaff};
    private static final String[] sColorModes = {"SrcDst均不透", "Src半透Dst不透", "Src不透Dst半透", "SrcDst均半透"};

    private static final Xfermode[] sModes = {
            new PorterDuffXfermode(PorterDuff.Mode.CLEAR),
            new PorterDuffXfermode(PorterDuff.Mode.SRC),
            new PorterDuffXfermode(PorterDuff.Mode.DST),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
            new PorterDuffXfermode(PorterDuff.Mode.DST_IN),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),
            new PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
            new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),
            new PorterDuffXfermode(PorterDuff.Mode.XOR),
            new PorterDuffXfermode(PorterDuff.Mode.DARKEN),
            new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
            new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
            new PorterDuffXfermode(PorterDuff.Mode.SCREEN),
            new PorterDuffXfermode(PorterDuff.Mode.ADD),
            new PorterDuffXfermode(PorterDuff.Mode.OVERLAY)
    };

    private static final String[] sLabels = {
            "Clear", "Src", "Dst", "SrcOver",
            "DstOver", "SrcIn", "DstIn", "SrcOut",
            "DstOut", "SrcATop", "DstATop", "Xor",
            "Darken", "Lighten", "Multiply", "Screen",
            "ADD", "OVERLAY"
    };

    public XfermodeView(Context context) {
        super(context);
        init(null, 0);
    }

    public XfermodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public XfermodeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        if(Build.VERSION.SDK_INT >= 11){
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置背景色
        canvas.drawARGB(255, 139, 197, 186);

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        int maxRows = sModes.length/4 + 1;
        for(int row = 0; row < maxRows; row++){
            for(int column = 0; column < 4 && (row * 4 + column) < sModes.length; column++){
                canvas.save();
//                int layer = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
                paint.setXfermode(null);
                int index = row * 4 + column;
                float translateX = (cellSize + cellHorizontalOffset) * column;
                float translateY = (cellSize + cellVerticalOffset) * row;
                canvas.translate(translateX, translateY);
                //画文字
                String text = sLabels[index];
                paint.setColor(Color.BLACK);
                float textXOffset = cellSize / 2;
                float textYOffset = textSize + (cellVerticalOffset - textSize) / 2;
                canvas.drawText(text, textXOffset, textYOffset, paint);
                canvas.translate(0, cellVerticalOffset);
                //画边框
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(0xff000000);
                canvas.drawRect(2, 2, cellSize - 2, cellSize - 2, paint);
                paint.setStyle(Paint.Style.FILL);
                //画圆
                paint.setColor(circleColors[colorModeIndex]);
                float left = circleRadius + 3;
                float top = circleRadius + 3;
                canvas.drawCircle(left, top, circleRadius, paint);
                paint.setXfermode(sModes[index]);
                //画矩形
                paint.setColor(rectColors[colorModeIndex]);
                float rectRight = circleRadius + rectSize;
                float rectBottom = circleRadius + rectSize;
                canvas.drawRect(left, top, rectRight, rectBottom, paint);
                paint.setXfermode(null);
                canvas.restore();
//                canvas.restoreToCount(layer);
            }
        }

        canvas.save();
//        int layer = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
        int column = sModes.length % 4;
        int row = column ==0 ? sModes.length/4 + 1 : sModes.length/4;
        float translateX = (cellSize + cellHorizontalOffset) * column;
        float translateY = (cellSize + cellVerticalOffset) * row;
        canvas.translate(translateX, translateY);

        //画边框
        canvas.translate(0, cellVerticalOffset);
        paint.setColor(Color.GREEN);
        canvas.drawRect(2, 2, cellSize - 2, cellSize - 2, paint);

        //画文字
        String text = sColorModes[colorModeIndex];
        paint.setColor(Color.BLACK);
        float textXOffset = cellSize / 2;
        float textYOffset = cellSize / 2;
        canvas.drawText(text, textXOffset, textYOffset, paint);

        colorModesRect = new RectF(translateX, translateY, translateX + cellSize, translateY +cellSize);
        canvas.restore();
//        canvas.restoreToCount(layer);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cellSize = w / 4.5f;
        cellHorizontalOffset = cellSize / 6;
        cellVerticalOffset = cellSize * 0.426f;
        circleRadius = cellSize / 3;
        rectSize = cellSize * 0.6f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && colorModesRect.contains(event.getX(), event.getY())) {
            int temp = colorModeIndex + 1;
            colorModeIndex = temp > 3 ? 0 : temp;
            invalidate();
            return true;
        }else {
            return super.onTouchEvent(event);
        }
    }

}