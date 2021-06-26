package com.work.checkwork.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.work.checkwork.BaseActivity;
import com.work.checkwork.R;
import com.work.checkwork.dao.UserOpe;
import com.work.checkwork.model.DataBean;
import com.work.checkwork.model.UserBean;
import com.work.checkwork.preferences.AppPreferences;
import com.work.checkwork.utils.AppConstant;
import com.work.checkwork.utils.AppUtil;

import androidx.annotation.NonNull;
import de.hdodenhof.circleimageview.CircleImageView;


public class XiangActivity extends BaseActivity {

    private CircleImageView user_photo_view;
    private ImageView xiang_img;
    private TextView username_view, guanzhu;
    private DataBean mDataBean;
    private boolean isGuanZhu;
    private UserBean mUserBean;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_xiang;
    }

    @Override
    protected void onViewCreated() {
        mUserBean = AppPreferences.getParcelableEntity(this, AppConstant.EXTRA_USERINFO, UserBean.class);
        mDataBean = getIntent().getParcelableExtra("DataBean");
        user_photo_view = findViewById(R.id.user_photo_view);
        username_view = findViewById(R.id.username_view);
        xiang_img = findViewById(R.id.xiang_img);
        guanzhu = findViewById(R.id.guanzhu);

        AppUtil.loadImage(this, mDataBean.imgUrl, xiang_img);
        username_view.setText(mDataBean.userBean.userName);

        findViewById(R.id.button_back).setOnClickListener(this);
        findViewById(R.id.guanzhu).setOnClickListener(this);
        initData();
    }

    private void initData() {
        new Thread(() -> {
            isGuanZhu = UserOpe.getUserOpe().isGuanZhu(mUserBean.id, mDataBean.userId);
            Log.e("Lance", "initData: isGuanZhu --->" + isGuanZhu );
            Message msg = handler.obtainMessage();
            msg.what = 0;
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
            case R.id.button_back:
                finish();
                break;
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
                if (UserOpe.getUserOpe().delGuanZhu(mUserBean.id, mDataBean.id) == 1) {
                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            } else {
                if (UserOpe.getUserOpe().addGuanZhu(mUserBean.id, mDataBean.id) == 1) {
                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }
}