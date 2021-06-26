package com.work.checkwork.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.work.checkwork.BaseActivity;
import com.work.checkwork.R;
import com.work.checkwork.adapter.GuanZhuAdapter;
import com.work.checkwork.dao.UserOpe;
import com.work.checkwork.model.UserBean;
import com.work.checkwork.preferences.AppPreferences;
import com.work.checkwork.utils.AppConstant;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GuanZhuListActivity extends BaseActivity {

    private TextView mTitle;
    private ArrayList<UserBean> userList;
    private RecyclerView mPagerRecycler;
    private GuanZhuAdapter mGuanZhuAdapter;
    private UserBean mUserBean;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_guanzhu;
    }

    @Override
    protected void onViewCreated() {
        userList = new ArrayList<>();
        mTitle = findViewById(R.id.textview_title);
        mTitle.setText("我的关注");
        mUserBean = AppPreferences.getParcelableEntity(this, AppConstant.EXTRA_USERINFO, UserBean.class);
        mPagerRecycler = findViewById(R.id.pager_recycler);
        mPagerRecycler.setLayoutManager(new LinearLayoutManager(this));
        mGuanZhuAdapter = new GuanZhuAdapter(this);
        mPagerRecycler.setAdapter(mGuanZhuAdapter);
        findViewById(R.id.button_back).setOnClickListener(this);
        initData();
    }

    private void initData() {
        new Thread(() -> {
            userList = UserOpe.getUserOpe().getGuanZhu(mUserBean.id);
            Message msg = handler.obtainMessage();
            msg.what = 0;
            msg.obj = userList;
            handler.sendMessage(msg);
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    mGuanZhuAdapter.replaceAllItems((ArrayList<UserBean>) msg.obj);
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        finish();
    }
}