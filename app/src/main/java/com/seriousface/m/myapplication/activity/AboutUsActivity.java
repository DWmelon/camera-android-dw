package com.seriousface.m.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.seriousface.m.myapplication.R;

/**
 * Created by i on 2016/4/19.
 */
public class AboutUsActivity extends Activity implements View.OnClickListener{

    TextView title;
    View back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        title = (TextView)findViewById(R.id.tv_top_title);
        back = findViewById(R.id.iv_back);

        title.setText(getString(R.string.about_us_contact));

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
}
