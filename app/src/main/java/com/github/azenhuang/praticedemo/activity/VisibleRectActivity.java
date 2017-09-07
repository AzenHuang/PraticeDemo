package com.github.azenhuang.praticedemo.activity;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.azenhuang.praticedemo.R;

/**
 * Created by huangyongzheng on 17/5/17.
 */

public class VisibleRectActivity extends AppCompatActivity {
    private int lastX = 0;
    private int lastY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_visible_rect);

        ImageView imageView = (ImageView) findViewById(R.id.img);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;

                        int left = v.getLeft() + dx;
                        int top = v.getTop() + dy;
                        int right = v.getRight() + dx;
                        int bottom = v.getBottom() + dy;

                        v.layout(left, top, right, bottom);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();

                        Rect localRect = new Rect();
                        v.getLocalVisibleRect(localRect);
                        ((TextView) findViewById(R.id.local))
                                .setText("local" + localRect.toString());

                        Rect globalRect = new Rect();
                        Point globalOffset = new Point();
                        v.getGlobalVisibleRect(globalRect, globalOffset);
                        ((TextView) findViewById(R.id.global))
                                .setText("global" + globalRect.toString());
                        ((TextView) findViewById(R.id.offset))
                                .setText("globalOffset:" + globalOffset.x + "," + globalOffset.y);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });


    }
}
