package com.fstyle.androidtrainning.screen.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fstyle.androidtrainning.screen.favourite.FavouriteActivity;
import com.fstyle.androidtrainning.screen.profile.ProfileActivity;
import com.fstyle.androidtrainning.screen.search.SearchActivity;


/**
 * Created by MyPC on 20/12/2017.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    private static final int TAB_HOME = 0;
    private static final int TAB_SEARCH = 1;
    private static final int TAB_FAVOURITE = 2;
    private static final int TAB_PROFILE = 3;
    int mNumOfTab;


    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTab = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case TAB_HOME:
                HomeFragment frmHome = new HomeFragment();
                return frmHome;
            case TAB_SEARCH:
                SearchActivity frmSearch = new SearchActivity();
                return frmSearch;
            case TAB_FAVOURITE:
                FavouriteActivity frmFavourite = new FavouriteActivity();
                return frmFavourite;
            case TAB_PROFILE:
                ProfileActivity frmProfile = new ProfileActivity();
                return frmProfile;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTab;
    }
}
