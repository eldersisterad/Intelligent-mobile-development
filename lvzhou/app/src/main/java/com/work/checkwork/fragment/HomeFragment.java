package com.work.checkwork.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.work.checkwork.R;
import com.work.checkwork.activity.SouActivity;
import com.work.checkwork.adapter.PagerAdapter;
import com.work.checkwork.fragment.home.ComicFragment;
import com.work.checkwork.fragment.home.Dancefragment;
import com.work.checkwork.fragment.home.DietFragment;
import com.work.checkwork.fragment.home.FashionFragment;
import com.work.checkwork.fragment.home.FolkloreFragment;
import com.work.checkwork.fragment.home.SculptureFragment;
import com.work.checkwork.fragment.home.SportsFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

/**
 * Home
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private TabLayout myTab;
    private ViewPager2 myPager2;
    private LinearLayout layoyt_sou;

    private PagerAdapter mPagerAdapter;
    List<String> titles = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();

    private FashionFragment mFashionFragment;
    private SculptureFragment mSculptureFragment;
    private Dancefragment mDancefragment;
    private ComicFragment mComicFragment;
    private DietFragment mDietFragment;
    private FolkloreFragment mFolkloreFragment;
    private SportsFragment mSportsFragment;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        myTab = findViewById(R.id.record_tabs);
        myPager2 = findViewById(R.id.my_viewpager);
        layoyt_sou = findViewById(R.id.layoyt_sou);

        //添加标题 时尚、雕塑，舞蹈、动漫、饮食、民俗、体育
        titles.add("时尚");
        titles.add("雕塑");
        titles.add("舞蹈");
        titles.add("动漫");
        titles.add("饮食");
        titles.add("民俗");
        titles.add("体育");
        mFashionFragment = new FashionFragment();
        mSculptureFragment = new SculptureFragment();
        mDancefragment = new Dancefragment();
        mComicFragment = new ComicFragment();
        mDietFragment = new DietFragment();
        mFolkloreFragment = new FolkloreFragment();
        mSportsFragment = new SportsFragment();
        //添加Fragment进去
        fragments.add(mFashionFragment);
        fragments.add(mSculptureFragment);
        fragments.add(mDancefragment);
        fragments.add(mComicFragment);
        fragments.add(mDietFragment);
        fragments.add(mFolkloreFragment);
        fragments.add(mSportsFragment);

        mPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager(), getActivity().getLifecycle(), fragments);
        myPager2.setAdapter(mPagerAdapter);

        //TabLayout和Viewpager2进行关联
        new TabLayoutMediator(myTab, myPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles.get(position));
            }
        }).attach();
        layoyt_sou.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(getContext(), SouActivity.class));
    }

    public void up() {
        mPagerAdapter.notifyDataSetChanged();
    }
}