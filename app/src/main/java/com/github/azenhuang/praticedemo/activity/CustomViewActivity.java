package com.github.azenhuang.praticedemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by huangyongzheng on 2/10/17.
 */

public class CustomViewActivity extends AppCompatActivity {
    public static final String EXTRA_LAYOUT_RES_ID = "extra_layout_resource_id";
    public static final String EXTRA_VIEW_CLASS = "extra_view_class";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView();

    }

    private void setContentView() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(EXTRA_LAYOUT_RES_ID)) {
                setContentView(intent.getIntExtra(EXTRA_LAYOUT_RES_ID, -1));
            } else if (intent.hasExtra(EXTRA_VIEW_CLASS)){
                setContentView(makeCustomView(), new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            }
        }
    }

    private View makeCustomView(){
        Intent intent = getIntent();
        if (intent != null) {
            Class viewClass = (Class) intent.getSerializableExtra(EXTRA_VIEW_CLASS);
            if (viewClass != null) {
                try {
                    Constructor<? extends View> viewConstructor = viewClass.getDeclaredConstructor(Context.class);

                    View customView = viewConstructor.newInstance(this);
                    return customView;
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public static Bundle makeExtraBundle(int layoutResId){
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_LAYOUT_RES_ID, layoutResId);
        return bundle;
    }

    public static Bundle makeExtraBundle(Class<? extends View> customViewClass){
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_VIEW_CLASS, customViewClass);
        return bundle;
    }

}
