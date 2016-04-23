package com.seriousface.m.myapplication.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.seriousface.m.myapplication.R;
import com.seriousface.m.myapplication.adapter.ViewPagerAdapter;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2016/4/14.
 */
public class FaceTypeActivity extends FragmentActivity implements View.OnClickListener{

    TextView title;
    TabLayout tabLayout;
    ViewPager viewPager;

    ViewPagerAdapter adapter;

    View back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_type);

        tabLayout = (TabLayout) findViewById(R.id.tl_main);
        viewPager = (ViewPager) findViewById(R.id.vp_main);
        title = (TextView)findViewById(R.id.tv_top_title);
        back = findViewById(R.id.iv_back);

        title.setText(getString(R.string.primary_title_official));

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
