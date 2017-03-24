package com.github.azenhuang.praticedemo.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.azenhuang.praticedemo.R;
import com.github.azenhuang.praticedemo.layout.TagsLayout;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by huangyongzheng on 2/28/17.
 */

public class TagsLayoutActivity extends AppCompatActivity {
    TagsLayout tagsLayout;
    ArrayList<String> dataList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_flow);
        tagsLayout = (TagsLayout) findViewById(R.id.tags_layout);
        setData();
    }

    private void setData() {
        initData();
        setTags();
    }



    private void initData() {
        dataList = new ArrayList<>();
        dataList.add("测试");
        dataList.add("a");
        dataList.add("中华人民共和国总有人名字");
        dataList.add("短时");
        dataList.add("阿百川的傅国辉");
        dataList.add("abcdefghijjkl");
        dataList.add("ji");
        dataList.add("好好说话");
        dataList.add("你好");
        dataList.add("世界吧");

    }

    public void setTags() {
        int dataSize = dataList.size();
        int childCount = tagsLayout.getChildCount();
        //当前child比dataSize多出的个数
        int extraChildCount = childCount - dataSize;
        if (extraChildCount > 0) {
            tagsLayout.removeViews(0, extraChildCount);
            for (int i = 0; i < dataSize; i++) {
                setTagContent(tagsLayout.getChildAt(i), dataList.get(i));
            }
        } else { //child少于data的个数
            for (int i = 0; i < childCount; i++) {
                setTagContent(tagsLayout.getChildAt(i), dataList.get(i));
            }

            for (int i = childCount; i < dataSize; i++) {
                View newChild = LayoutInflater.from(this).inflate(R.layout.search_tag_cloud_item, tagsLayout, false);
                setTagContent(newChild, dataList.get(i));
                tagsLayout.addView(newChild);
            }
        }
        tagsLayout.requestLayout();
    }

    private void setTagContent(View childView, final String tagData) {
        if (TextUtils.isEmpty(tagData)) {
            childView.setVisibility(GONE);
            return;
        }
        childView.setVisibility(VISIBLE);
        ViewHolder holder = (ViewHolder) childView.getTag();
        if (holder == null) {
            holder = new ViewHolder(childView);
        }
        holder.tagTextView.setText(tagData);

        childView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataList.remove(tagData);
                dataList.add(0, tagData);
                setTags();
            }
        });
    }

    private static class ViewHolder {
        ImageView tagImageView;
        TextView tagTextView;

        public ViewHolder(View view) {
            tagImageView = (ImageView) view.findViewById(R.id.tag_image);
            tagTextView = (TextView) view.findViewById(R.id.tag_text);
        }
    }
}
