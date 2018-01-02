package com.fstyle.androidtrainning.screen.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.fstyle.androidtrainning.R;

public class MainActivity extends AppCompatActivity{

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private int[] tabIcons = {
            R.drawable.ic_local_movies_white_36dp,
            R.drawable.ic_search_grey,
            R.drawable.ic_tab_favourite,
            R.drawable.ic_user
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initViews();
        handleEvents();
    }

    private void initViews() {
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.title_tab_home));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.title_tab_search));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.title_tab_favourite));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.title_tab_profile));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.getTabAt(0).setIcon(tabIcons[0]);
        mTabLayout.getTabAt(1).setIcon(tabIcons[1]);
        mTabLayout.getTabAt(2).setIcon(tabIcons[2]);
        mTabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    private void handleEvents() {
        mViewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),
                mTabLayout.getTabCount());
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}
