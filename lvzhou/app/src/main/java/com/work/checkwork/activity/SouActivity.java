package com.work.checkwork.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.work.checkwork.BaseActivity;
import com.work.checkwork.R;
import com.work.checkwork.adapter.FashionAdapter;
import com.work.checkwork.dao.DBService;
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


public class SouActivity extends BaseActivity {

    private TextView mTitle;

    private EditText et_sousuo;
    private TextView sousuo;

    private ArrayList<DataBean> mDataList;
    private RecyclerView mPagerRecycler;
    private FashionAdapter mFashionAdapter;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_sou;
    }

    @Override
    protected void onViewCreated() {
        mTitle = findViewById(R.id.textview_title);
        mTitle.setText("搜索");

        mPagerRecycler = findViewById(R.id.pager_recycler);
        et_sousuo = findViewById(R.id.et_sousuo);
        sousuo = findViewById(R.id.sousuo);

        mFashionAdapter = new FashionAdapter(this);
        //2.声名为瀑布流的布局方式: 2列,垂直方向
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mPagerRecycler.setLayoutManager(mStaggeredGridLayoutManager);
        mPagerRecycler.setAdapter(mFashionAdapter);
        sousuo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(et_sousuo.getText().toString())) {
            Toast.makeText(this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
            return;
        }
        souData(et_sousuo.getText().toString());
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