package com.chogoon.cglib;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public final class CgOnTabSelectedListener implements TabLayout.OnTabSelectedListener {

    private final ViewPager viewPager;

    public CgOnTabSelectedListener(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
