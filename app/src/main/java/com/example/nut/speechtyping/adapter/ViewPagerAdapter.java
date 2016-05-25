package com.example.nut.speechtyping.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.example.nut.speechtyping.fragment.FirstTabFragment;
import com.example.nut.speechtyping.fragment.SecondTabFragment;

/**
 * Created by Nut on 4/6/2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence titles[];  // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int numbOfTabs;         // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    public ViewPagerAdapter(FragmentManager fm, CharSequence Titles[], int numbOfTabs) {
        super(fm);

        this.titles = Titles;
        this.numbOfTabs = numbOfTabs;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            FirstTabFragment tab1 = FirstTabFragment.newInstance();
            return tab1;
        } else {
            SecondTabFragment tab2 = SecondTabFragment.newInstance();
            return tab2;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    // This method return the titles for the Tabs in the Tab Strip
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    // This method return the Number of tabs for the tabs Strip
    @Override
    public int getCount() {
        return numbOfTabs;
    }
}
