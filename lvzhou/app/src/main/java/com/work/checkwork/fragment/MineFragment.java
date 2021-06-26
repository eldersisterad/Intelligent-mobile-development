package com.work.checkwork.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.work.checkwork.R;
import com.work.checkwork.activity.GuanZhuListActivity;
import com.work.checkwork.activity.UserInfoActivity;
import com.work.checkwork.adapter.PagerAdapter;
import com.work.checkwork.fragment.mine.MyLikeFragment;
import com.work.checkwork.fragment.mine.MyReleaseFragment;
import com.work.checkwork.login.LoginActivity;
import com.work.checkwork.model.UserBean;
import com.work.checkwork.preferences.AppPreferences;
import com.work.checkwork.utils.AppConstant;
import com.work.checkwork.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Mine
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private TextView username_view, tick_view;
    private Button login_out;

    private UserBean mUserBean;
    private TextView add_class_room;
    private CircleImageView mUserPic;

    private TabLayout myTab;
    private ViewPager2 myPager2;

    private PagerAdapter mPagerAdapter;
    List<String> titles = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        username_view = findViewById(R.id.username_view);
        tick_view = findViewById(R.id.tick_view);
        mUserPic = findViewById(R.id.user_photo_view);
        login_out = findViewById(R.id.login_out);
        add_class_room = findViewById(R.id.add_class_room);
        myTab = findViewById(R.id.record_tabs);
        myPager2 = findViewById(R.id.my_viewpager);


        if ((Boolean) AppPreferences.get(mContext, AppConstant.EXTRA_ISLOGIN, false)) {
            login_out.setVisibility(View.VISIBLE);
        } else {
            login_out.setVisibility(View.GONE);
        }

        titles.add("我的发布");
        titles.add("我的收藏");

        //添加Fragment进去
        fragments.add(new MyReleaseFragment());
        fragments.add(new MyLikeFragment());

        mPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager(), getActivity().getLifecycle(), fragments);
        myPager2.setAdapter(mPagerAdapter);

        //TabLayout和Viewpager2进行关联
        new TabLayoutMediator(myTab, myPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles.get(position));
            }
        }).attach();

        findViewById(R.id.userinfo_layout).setOnClickListener(this);
        findViewById(R.id.video_setting_view).setOnClickListener(this);
        login_out.setOnClickListener(this);
        tick_view.setOnClickListener(this);
        add_class_room.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        if ((boolean) AppPreferences.get(getActivity(), AppConstant.EXTRA_ISLOGIN, false)) {
            mUserBean = AppPreferences.getParcelableEntity(mContext, AppConstant.EXTRA_USERINFO, UserBean.class);
            username_view.setText(mUserBean.userName);
            AppUtil.loadImage(getContext(), mUserBean.userPic, mUserPic);
        } else {
            username_view.setText("请登录");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tick_view:
                startActivity(new Intent(getContext(), GuanZhuListActivity.class));
                break;
            case R.id.video_setting_view:
                if (!(Boolean) AppPreferences.get(mContext, AppConstant.EXTRA_ISLOGIN, false)) {
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                    return;
                }
                startActivity(new Intent(mContext, UserInfoActivity.class));
                break;
            case R.id.login_out:
                AppPreferences.put(mContext, AppConstant.EXTRA_ISLOGIN, false);
                AppPreferences.clear(mContext);
                startActivity(new Intent(mContext, LoginActivity.class));
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}