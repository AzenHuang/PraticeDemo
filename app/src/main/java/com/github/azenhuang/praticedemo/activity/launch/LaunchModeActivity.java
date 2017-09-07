package com.github.azenhuang.praticedemo.activity.launch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.github.azenhuang.praticedemo.R;
import com.github.azenhuang.praticedemo.activity.LifecycleLogActivity;

/**
 * Created by huangyongzheng on 3/24/17.
 */

public class LaunchModeActivity extends LifecycleLogActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lanuch_mode);

        ((TextView) findViewById(R.id.title)).setText(getClass().getSimpleName());

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LaunchModeActivity.this, StandardActivity.class);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.hyz");
                intent.addCategory("android.intent.category.hyz");
                startActivity(intent);
            }
        });


        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchModeActivity.this, SingleTopActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchModeActivity.this, SingleTaskActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchModeActivity.this, SingleInstanceActivity.class);
                startActivity(intent);
            }
        });

    }
}
