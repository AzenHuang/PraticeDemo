package com.github.azenhuang.praticedemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.github.azenhuang.praticedemo.R;
import com.github.azenhuang.praticedemo.view.RoundImageView;


public class RoundImageViewActivity extends AppCompatActivity {
    private RoundImageView mQiQiu;
    private RoundImageView mMeiNv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_image);

        mQiQiu = (RoundImageView) findViewById(R.id.id_qiqiu);
        mMeiNv = (RoundImageView) findViewById(R.id.id_meinv);

        mQiQiu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mQiQiu.setType(RoundImageView.TYPE_ROUND);
            }
        });

        mMeiNv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mMeiNv.setBorderRadius(90);
            }
        });
    }

}
