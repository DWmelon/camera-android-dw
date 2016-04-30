package com.seriousface.m.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seriousface.m.myapplication.R;
import com.seriousface.m.myapplication.util.EmailUtil;

/**
 * Created by i on 2016/4/19.
 */
public class AdviceActivity extends BaseActivity implements View.OnClickListener{

    TextView title;
    View back;
    ImageView ivSendEmail;

    EditText mAdviceContent;
    EditText mAdviceQQ;
    EmailUtil m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        title = (TextView)findViewById(R.id.tv_top_title);
        back = findViewById(R.id.iv_back);
        ivSendEmail = (ImageView)findViewById(R.id.iv_top_right);
        mAdviceContent = (EditText)findViewById(R.id.et_feedback_content);
        mAdviceQQ = (EditText)findViewById(R.id.et_qq_content);

        ivSendEmail.setImageResource(R.drawable.icon_send_mail_gray);
        title.setText(getString(R.string.about_us_advice));
        back.setOnClickListener(this);
        ivSendEmail.setOnClickListener(this);

        mAdviceContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    ivSendEmail.setImageResource(R.drawable.icon_send_mail_gray);
                }else{
                    ivSendEmail.setImageResource(R.drawable.icon_send_mail);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_top_right:
                checkAndSend();
                break;
        }
    }

    private void checkAndSend(){
        if(mAdviceContent.getText().toString().trim().isEmpty()){
            Toast.makeText(this,getString(R.string.advice_empty_tip),Toast.LENGTH_LONG).show();
        }else{
            sendEmail();
        }
    }

    private void sendEmail(){
                m = new EmailUtil("375591580@qq.com", "ggrsufgachusbjjg");

                String[] toArr = {"3062016046@qq.com"};
                m.setTo(toArr);
                m.setFrom("375591580@qq.com");
                m.setSubject(mAdviceQQ.getText().toString().trim()+"的反馈");
                m.setBody(mAdviceContent.getText().toString().trim());
                try {
                    if(m.send()) {
                        Toast.makeText(AdviceActivity.this, "发送成功啦~", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(AdviceActivity.this, "发送失败>.<", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
    }

}
