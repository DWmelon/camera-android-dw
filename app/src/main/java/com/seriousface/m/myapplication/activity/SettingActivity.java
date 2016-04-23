package com.seriousface.m.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seriousface.m.myapplication.R;

/**
 * Created by i on 2016/4/19.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener{

    TextView tvTitle;

    RelativeLayout mAdvice;
    RelativeLayout mContact;

    View mBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        tvTitle = (TextView)findViewById(R.id.tv_top_title);
        mBack = findViewById(R.id.iv_back);
        mAdvice = (RelativeLayout)findViewById(R.id.rl_about_advice);
        mContact = (RelativeLayout)findViewById(R.id.rl_about_contact);

        tvTitle.setText(getString(R.string.primary_title_about_us));

        mAdvice.setOnClickListener(this);
        mContact.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_about_advice:
                i = new Intent(SettingActivity.this,AdviceActivity.class);
                startActivity(i);
                break;
            case R.id.rl_about_contact:
                i = new Intent(SettingActivity.this,AboutUsActivity.class);
                startActivity(i);
                break;

        }
    }
}
