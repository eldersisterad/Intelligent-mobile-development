package com.work.checkwork.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.work.checkwork.BaseActivity;
import com.work.checkwork.R;
import com.work.checkwork.adapter.FashionAdapter;
import com.work.checkwork.dao.DBService;
import com.work.checkwork.dao.UserOpe;
import com.work.checkwork.model.DataBean;
import com.work.checkwork.model.UserBean;
import com.work.checkwork.preferences.AppPreferences;
import com.work.checkwork.utils.AppConstant;
import com.work.checkwork.utils.AppUtil;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import de.hdodenhof.circleimageview.CircleImageView;


public class UserHomeActivity extends BaseActivity {

    private TextView username_view, guanzhu;
    private CircleImageView mUserPic;

    private UserBean mUserBean;
    private UserBean mHeBean;

    private boolean isGuanZhu;
    private RecyclerView mPagerRecycler;
    private FashionAdapter mFashionAdapter;
    private ArrayList<DataBean> mDataList;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_userhome;
    }

    @Override
    protected void onViewCreated() {
        username_view = findViewById(R.id.username_view);
        mUserPic = findViewById(R.id.user_photo_view);
        guanzhu = findViewById(R.id.guanzhu);
        mPagerRecycler = findViewById(R.id.pager_recycler);
        mHeBean = getIntent().getParcelableExtra("UserBean");
        mUserBean = AppPreferences.getParcelableEntity(this, AppConstant.EXTRA_USERINFO, UserBean.class);
        username_view.setText(mHeBean.userName);
        AppUtil.loadImage(this, mHeBean.userPic, mUserPic);

        mFashionAdapter = new FashionAdapter(this);
        //2.声名为瀑布流的布局方式: 2列,垂直方向
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mPagerRecycler.setLayoutManager(mStaggeredGridLayoutManager);
        mPagerRecycler.setAdapter(mFashionAdapter);
        guanzhu.setOnClickListener(this);
        initData();
    }


    private void initData() {
        new Thread(() -> {
            mDataList = DBService.getDbService().getMyData(mHeBean.id);
            isGuanZhu = UserOpe.getUserOpe().isGuanZhu(mUserBean.id, mHeBean.id);
            Message msg = handler.obtainMessage();
            msg.what = 0;
            msg.obj = mDataList;
            msg.arg1 = isGuanZhu ? 1 : 0;
            handler.sendMessage(msg);
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    mFashionAdapter.replaceAllItems((ArrayList<DataBean>) msg.obj);
                    guanzhu.setText(msg.arg1 == 1 ? "已关注" : "+ 关注");
                    break;
                case 1:
                    initData();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.guanzhu:
                guanzhu(isGuanZhu);
                break;
            default:
                break;
        }
    }

    private void guanzhu(boolean isGuanZhu) {
        new Thread(() -> {
            if (isGuanZhu) {
                if (UserOpe.getUserOpe().delGuanZhu(mUserBean.id, mHeBean.id) == 1) {
                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            } else {
                if (UserOpe.getUserOpe().addGuanZhu(mUserBean.id, mHeBean.id) == 1) {
                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }
}