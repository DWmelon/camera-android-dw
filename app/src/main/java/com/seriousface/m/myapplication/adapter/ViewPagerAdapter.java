package com.seriousface.m.myapplication.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.seriousface.m.myapplication.constant.Constant;
import com.seriousface.m.myapplication.fragment.FaceFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/14.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    List<FaceFragment> fragments = new ArrayList<>();
    String[] titles = {"明星","熊本熊","emoji"};

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        init();
    }


    private void init(){
        Bundle bundle1 = new Bundle();
        bundle1.putString(Constant.KEY_PIC_PAGE_TYPE,Constant.VALUE_PIC_PAGE_TYPE_STAR);
        FaceFragment fragment1 = FaceFragment.newInstant(bundle1);
        fragments.add(fragment1);

        Bundle bundle2 = new Bundle();
        bundle2.putString(Constant.KEY_PIC_PAGE_TYPE,Constant.VALUE_PIC_PAGE_TYPE_XIONG_BEN);
        FaceFragment fragment2 = FaceFragment.newInstant(bundle2);
        fragments.add(fragment2);

        Bundle bundle3 = new Bundle();
        bundle3.putString(Constant.KEY_PIC_PAGE_TYPE,Constant.VALUE_PIC_PAGE_TYPE_EMOJI);
        FaceFragment fragment3 = FaceFragment.newInstant(bundle3);
        fragments.add(fragment3);

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
