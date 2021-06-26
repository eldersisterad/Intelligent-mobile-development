package com.work.checkwork.fragment.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.work.checkwork.R;
import com.work.checkwork.adapter.FashionAdapter;
import com.work.checkwork.dao.DBService;
import com.work.checkwork.fragment.BaseFragment;
import com.work.checkwork.model.DataBean;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Created by Android Studio.
 * 雕塑
 */
public class SculptureFragment extends BaseFragment {

    private RecyclerView mPagerRecycler;
    private FashionAdapter mFashionAdapter;
    private ArrayList<DataBean> mDataList;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_pager;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPagerRecycler = findViewById(R.id.pager_recycler);
        mFashionAdapter = new FashionAdapter(getContext());
        //2.声名为瀑布流的布局方式: 2列,垂直方向
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mPagerRecycler.setLayoutManager(mStaggeredGridLayoutManager);
        mPagerRecycler.setAdapter(mFashionAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        new Thread(() -> {
            mDataList = (ArrayList<DataBean>) DBService.getDbService().getData(1);
            Message msg = handler.obtainMessage();
            msg.what = 0;
            msg.obj = mDataList;
            handler.sendMessage(msg);
        }).start();
    }

    public void souData(String souContent) {
        new Thread(() -> {
            mDataList = DBService.getDbService().getSouData(souContent);
            Message msg = handler.obtainMessage();
            msg.what = 0;
            msg.obj = mDataList;
            handler.sendMessage(msg);
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    if (mFashionAdapter != null) {
                        mFashionAdapter.replaceAllItems((ArrayList<DataBean>) msg.obj);
                    }
                    break;
                default:
                    break;
            }
        }
    };

} 