package com.seriousface.m.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.seriousface.m.myapplication.R;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    TextView tvOfficial;
    TextView tvOwn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvOfficial = (TextView)findViewById(R.id.tv_main_official);
        tvOwn = (TextView)findViewById(R.id.tv_main_own);

        tvOfficial.setOnClickListener(this);
        tvOwn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.tv_main_official:
                i = new Intent(MainActivity.this,FaceTypeActivity.class);
                MainActivity.this.startActivity(i);
                break;
            case R.id.tv_main_own:
                i = new Intent(MainActivity.this,FaceTypeActivity.class);
                MainActivity.this.startActivity(i);
                break;
        }
    }
}
