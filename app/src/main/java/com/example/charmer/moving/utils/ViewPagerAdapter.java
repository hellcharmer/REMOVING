package com.example.charmer.moving.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.charmer.moving.PageFragment;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[]{"文章", "收藏", "发布", "参加"};
    private Context context;
    private String user;


    public ViewPagerAdapter(FragmentManager fm, Context context,String user) {
        super(fm);
        this.context = context;
        this.user = user;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1,user);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}