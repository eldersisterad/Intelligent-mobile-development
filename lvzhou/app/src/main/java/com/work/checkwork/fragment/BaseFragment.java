package com.work.checkwork.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import com.work.checkwork.R;
import androidx.fragment.app.Fragment;
/**
 *  所有Fragment的抽象层
 *
 *    前端
 *      html css js  -----> 大玩具vue ----> 虚拟DOM   每一次操作 虚拟DOM ---->DOM
 *
 *      mvc / mvvm
 *
 */
public class BaseFragment  extends Fragment {

    public Context mContext;
    private View mRootView;

    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mRootView = inflater.inflate(getLayoutResourceId(), container, false);
    }

    protected int getLayoutResourceId() {
        return R.layout.fragment_empty;
    }

    @CallSuper
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mRootView == null) {
            mRootView = view;
        }
    }

    public <T extends View> T findViewById(@IdRes int id) {
        if (mRootView != null) {
            return ((T) mRootView.findViewById(id));
        } else {
            throw new NullPointerException("you should not delete super.onViewCreated(view, savedInstanceState) when you overwrite method onViewCreated");
        }
    }
}
