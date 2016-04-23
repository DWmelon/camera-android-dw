package com.seriousface.m.myapplication.activity;

import android.app.Activity;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by i on 2016/4/22.
 */
public class BaseActivity extends Activity {

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
