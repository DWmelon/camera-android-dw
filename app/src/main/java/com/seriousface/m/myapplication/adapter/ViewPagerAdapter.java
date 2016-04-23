package com.seriousface.m.myapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.seriousface.m.myapplication.fragment.FaceFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/14.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    List<FaceFragment> fragments = new ArrayList<>();
    String[] titles = {"emoji","熊本熊"};

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        init();
    }


    private void init(){
        for (int i = 0 ;i<2;i++){
            FaceFragment fragment = FaceFragment.newInstant(null);
            fragments.add(fragment);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
