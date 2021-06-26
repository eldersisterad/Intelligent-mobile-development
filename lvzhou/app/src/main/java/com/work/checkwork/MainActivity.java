package com.work.checkwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.work.checkwork.activity.ReleaseActivity;
import com.work.checkwork.fragment.BaseFragment;
import com.work.checkwork.fragment.HomeFragment;
import com.work.checkwork.fragment.MineFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvHome;
    private TextView mTvMine;
    private FragmentTransaction mFragmentTransaction;
    private MineFragment mMineFragment;
    private HomeFragment mHomeFragment;
    private FragmentManager mFragmentManager;

    /**
     * 启动app 加载启动activity 创建
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置 一下内容   xml  布局文件
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        // 写逻辑要更换
        // a ---> b
        // 找到需要点击的3个图标
        mTvHome = findViewById(R.id.tv_img_home);
        mTvMine = findViewById(R.id.tv_img_mine);

        // 给容器增加点击事件
        //找到容器
        RelativeLayout rlHome = findViewById(R.id.rl_tab_home);
        RelativeLayout rlMessage = findViewById(R.id.rl_tab_message);
        RelativeLayout rlMine = findViewById(R.id.rl_tab_mine);

        //默认选中的状态
        // 先找到 然后才能设置
        mTvHome.setBackgroundResource(R.drawable.comui_tab_home_selected);

        //  View.OnClickListener l
        rlHome.setOnClickListener(this);
        rlMessage.setOnClickListener(this);
        rlMine.setOnClickListener(this);

        // 创建Fragment  局部的显示的碎片
        mHomeFragment = new HomeFragment();

        //关联起来   在Activity 管理  Fragment  强耦合  设计中间类 管理
        //activity -----> manager -----> fragment
        // 切换 Fragment  原子性  同时成功 同时失败   事务
        mFragmentManager = getSupportFragmentManager();

        mFragmentTransaction = mFragmentManager.beginTransaction();
        // 导包  Fragment  androidx 整合到androidx   android
        mFragmentTransaction.replace(R.id.content_layout, mHomeFragment);
        // 事务内部 替换
        // 提交才是真正的替换
        mFragmentTransaction.commit();

    }

    @Override
    public void onClick(View view) {

        // 事务 引用错了
        // 不能和其他事务 混用
        // 事务的隔离级别

        // 点击谁 就会传进来谁
        int id = view.getId();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        switch (id) {
            // 当点击home
            case R.id.rl_tab_home:
                // 设置为黄色的图片
                // todo 切换到 HomeFragment
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    // 用事务统一管理切换
                    fragmentTransaction.add(R.id.content_layout, mHomeFragment);
                } else {
                    fragmentTransaction.show(mHomeFragment);
                }

                hideFragment(fragmentTransaction, mMineFragment);
                changesColors(mTvHome, R.drawable.comui_tab_home_selected);
                break;
            // 当点击的是message
            case R.id.rl_tab_message:
                startActivity(new Intent(this, ReleaseActivity.class));
                break;
            // 当点击mine
            case R.id.rl_tab_mine:
                System.out.println("mine 被点击了");
                // todo 切换到 MineFragment
                // 如果不存在 就创建
                // 如存在 show 展示就可以了

                hideFragment(fragmentTransaction, mHomeFragment);

                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                    fragmentTransaction.add(R.id.content_layout, mMineFragment);
                } else {
                    fragmentTransaction.show(mMineFragment);
                }

                // 需要修改谁，就传递谁
                changesColors(mTvMine, R.drawable.comui_tab_person_selected);
                break;

        }
        fragmentTransaction.commit();
    }

    //java 的多态  Mine Home Message
    private void hideFragment(FragmentTransaction f, BaseFragment fragment) {
        if (fragment != null) {
            f.hide(fragment);
        }
    }

    // 把需要改变的当做参数 传递进来
    private void changesColors(TextView changeTextView, int changeId) {
        // 首先 把所有的都设置为白色的
        mTvHome.setBackgroundResource(R.drawable.comui_tab_home);
        mTvMine.setBackgroundResource(R.drawable.comui_tab_person);
        // 把选中的设置为黄色的
        changeTextView.setBackgroundResource(changeId);
    }

}