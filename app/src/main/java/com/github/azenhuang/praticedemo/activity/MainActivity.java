package com.github.azenhuang.praticedemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.azenhuang.praticedemo.R;
import com.github.azenhuang.praticedemo.activity.launch.LaunchModeActivity;
import com.github.azenhuang.praticedemo.module.DemoInfo;
import com.github.azenhuang.praticedemo.view.LifecycleLogView;
import com.github.azenhuang.praticedemo.view.LifecycleLogViewGroup;
import com.github.azenhuang.praticedemo.view.RemoteControlMenuView;
import com.github.azenhuang.praticedemo.view.SearchView;
import com.github.azenhuang.praticedemo.view.XfermodeView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    private ArrayList<DemoInfo> demoInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDemoData();
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new MyAdapter());
    }

    private void initDemoData() {
        demoInfoList = new ArrayList<>();
        demoInfoList.add(new DemoInfo("MeituanLogoView xml", CustomViewActivity.class, CustomViewActivity.makeExtraBundle(R.layout.meituan_logo_view)));
        demoInfoList.add(new DemoInfo("RoundImageView", CustomViewActivity.class, CustomViewActivity.makeExtraBundle(R.layout.round_image_view)));
        demoInfoList.add(new DemoInfo("TitleBar", CustomViewActivity.class, CustomViewActivity.makeExtraBundle(R.layout.activity_custom_title_bar_demo)));

        demoInfoList.add(new DemoInfo("MtEditText", CustomViewActivity.class, CustomViewActivity.makeExtraBundle(R.layout.activity_editext_usage)));
        demoInfoList.add(new DemoInfo("XfermodeView", CustomViewActivity.class, CustomViewActivity.makeExtraBundle(XfermodeView.class)));
        demoInfoList.add(new DemoInfo("SearchView", CustomViewActivity.class, CustomViewActivity.makeExtraBundle(SearchView.class)));
        demoInfoList.add(new DemoInfo("RemoteControlMenuView", CustomViewActivity.class, CustomViewActivity.makeExtraBundle(RemoteControlMenuView.class)));
        demoInfoList.add(new DemoInfo("CustomTextView", CustomViewActivity.class, CustomViewActivity.makeExtraBundle(R.layout.activity_custom_text_view)));
        demoInfoList.add(new DemoInfo("TagsLayout", CustomViewActivity.class, CustomViewActivity.makeExtraBundle(R.layout.activity_tags_layout)));
        demoInfoList.add(new DemoInfo("FlowLayout", TagsLayoutActivity.class, CustomViewActivity.makeExtraBundle(R.layout.activity_tags_layout)));
        demoInfoList.add(new DemoInfo("HandlerActivity", HandlerActivity.class, null));
        demoInfoList.add(new DemoInfo("SafeHandlerActivity", SafeHandlerActivity.class, null));
        demoInfoList.add(new DemoInfo("AsyncTaskActivity", AsyncTaskActivity.class, null));
        demoInfoList.add(new DemoInfo("LaunchModeActivity", LaunchModeActivity.class, null));
        demoInfoList.add(new DemoInfo("VisibleRectActivity", VisibleRectActivity.class, null));
        demoInfoList.add(new DemoInfo("LifecycleLogView constructor", CustomViewActivity.class, CustomViewActivity.makeExtraBundle(LifecycleLogView.class)));
        demoInfoList.add(new DemoInfo("LifecycleLogView xml", CustomViewActivity.class, CustomViewActivity.makeExtraBundle(R.layout.lifecycle_view_layout)));
        demoInfoList.add(new DemoInfo("LifecycleLogViewGroup constructor", CustomViewActivity.class, CustomViewActivity.makeExtraBundle(LifecycleLogViewGroup.class)));
        demoInfoList.add(new DemoInfo("LifecycleLogViewGroup xml", CustomViewActivity.class, CustomViewActivity.makeExtraBundle(R.layout.lifecycle_viewgroup_layout)));
        demoInfoList.add(new DemoInfo("LifecycleLogViewGroupWithinView xml", CustomViewActivity.class, CustomViewActivity.makeExtraBundle(R.layout.lifecycle_viewgroup_within_view_layout)));
        demoInfoList.add(new DemoInfo("SecondaryTextView", CustomViewActivity.class, CustomViewActivity.makeExtraBundle(R.layout.secondary_text_demo)));
        demoInfoList.add(new DemoInfo("LaunchCountActivity", LaunchCountActivity.class, null));


    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements View.OnClickListener{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.recycler_item_demo, parent, false);
            view.setOnClickListener(this);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;

        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            DemoInfo demoInfo = demoInfoList.get(position);
            holder.clickTargetView.setTag(demoInfo);
            holder.demoNameText.setText(demoInfo.demoName);
        }

        @Override
        public int getItemCount() {
            return demoInfoList.size();
        }

        @Override
        public void onClick(View v) {
            DemoInfo demoInfo = (DemoInfo) v.getTag();
            Intent intent = new Intent(MainActivity.this, demoInfo.targetActivity);
            if (demoInfo.extraBundle != null) {
                intent.putExtras(demoInfo.extraBundle);
            }
            startActivity(intent);
        }


        class MyViewHolder extends RecyclerView.ViewHolder {
            View clickTargetView;
            TextView demoNameText;

            public MyViewHolder(View view) {
                super(view);
                clickTargetView = view;
                demoNameText = (TextView) view.findViewById(R.id.demo_name);
            }
        }


    }
}
