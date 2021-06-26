package com.work.checkwork;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置 一下内容   xml  布局文件
        int resId = getLayoutResourceId();
        if (resId >= 0) {
            setContentView(resId);
        } else {
            setContentView(createContentView(this), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }

        onViewCreated();
    }

    protected abstract void onViewCreated();

    protected int getLayoutResourceId() {
        return -1;
    }

    protected View createContentView(Context context) {
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        FrameLayout content = new FrameLayout(context);
        content.setId(android.R.id.custom);
        content.setBackgroundResource(R.color.result_image_border);
        rootLayout.addView(content, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        return rootLayout;
    }
}