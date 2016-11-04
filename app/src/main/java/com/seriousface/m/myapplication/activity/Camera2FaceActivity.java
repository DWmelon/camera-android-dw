package com.seriousface.m.myapplication.activity;

import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seriousface.m.myapplication.R;
import com.seriousface.m.myapplication.wxapi.WXShareManager;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by i on 2016/11/1.
 */

public class Camera2FaceActivity extends BaseActivity{

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    private TextView ivCameraTake;
    private ImageView ivExampleIcon;


    private ImageView ivCameraTurn;
    private RelativeLayout rlCamera1;
    private RelativeLayout rlCamera3;
    private ImageView tvCameraSave;
    private ImageView tvCameraReset;
    private TextView ivBack;
    private RelativeLayout tvCameraShare;

    private UMShareAPI mShareAPI;
    private WXShareManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_face);
        manager = WXShareManager.getInstance(this);
        initView();
    }

    private void initView(){
        ivBack = (TextView)findViewById(R.id.tv_camera_back);

        surfaceView = (SurfaceView)findViewById(R.id.sv_camera);
        ivCameraTake = (TextView)findViewById(R.id.tv_camera_take);
        ivCameraTurn = (ImageView)findViewById(R.id.iv_turn_photo);
        ivExampleIcon = (ImageView)findViewById(R.id.iv_compare_icon);

        rlCamera1 = (RelativeLayout)findViewById(R.id.rl_camera1);
        rlCamera3 = (RelativeLayout)findViewById(R.id.rl_camera3);
        tvCameraShare = (RelativeLayout)findViewById(R.id.tv_camera_share);
        tvCameraReset = (ImageView)findViewById(R.id.tv_camera_reset);
        tvCameraSave = (ImageView)findViewById(R.id.tv_camera_save);
    }

}
