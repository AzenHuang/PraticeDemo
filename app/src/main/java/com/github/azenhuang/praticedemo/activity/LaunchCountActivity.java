package com.github.azenhuang.praticedemo.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.github.azenhuang.praticedemo.R;

import java.lang.reflect.Method;

/**
 * Created by huangyongzheng on 17/8/11.
 */

public class LaunchCountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        TextView textView = (TextView) findViewById(R.id.text);
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
//        try {
//            Method method = ActivityManager.class.getDeclaredMethod("getAllPackageLaunchCounts", new Class[0]);
//            Map<String,Integer> apptimes = (Map<String, Integer>) method.invoke(am, new Object[0]);
//            Set<Map.Entry<String,Integer>> entryset = apptimes.entrySet();
//            Iterator<Map.Entry<String,Integer>> iterators = entryset.iterator();
//            while(iterators.hasNext()){
//                Map.Entry<String,Integer> item= iterators.next();
//
//                textView.append("key = "+item.getKey() +"  values ="+item.getValue());
//            }
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }

        Method[] methods = ActivityManager.class.getDeclaredMethods();
        for (Method m :
                methods) {
            textView.append(m.toString());
            textView.append("\n");
            Log.e("hyz", m.toString());
        }
    }
}
