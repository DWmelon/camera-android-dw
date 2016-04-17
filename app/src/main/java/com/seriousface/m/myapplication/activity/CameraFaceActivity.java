package com.seriousface.m.myapplication.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seriousface.m.myapplication.R;
import com.seriousface.m.myapplication.constant.Constant;
import com.seriousface.m.myapplication.wxapi.WXShareManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Administrator on 2016/4/15.
 */
public class CameraFaceActivity extends Activity implements View.OnClickListener{

    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera camera;
    byte[] photoData;
    ImageView ivCameraTake;
    ImageView ivExampleIcon;

    RelativeLayout rlCamera1;
    RelativeLayout rlCamera2;
    RelativeLayout rlCamera3;
    TextView tvCamera2;
    TextView tvCameraSave;
    TextView tvCameraReset;
    TextView tvCameraShare;

    int originalNum = -1;
    WXShareManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_face);
        manager = WXShareManager.getInstance(this);
        initView();
        initData();
        initSurfaceView();
    }

    private void initView(){
        surfaceView = (SurfaceView)findViewById(R.id.sv_camera);
        ivCameraTake = (ImageView)findViewById(R.id.iv_camera_take);
        ivExampleIcon = (ImageView)findViewById(R.id.iv_compare_icon);

        rlCamera1 = (RelativeLayout)findViewById(R.id.rl_camera1);
        rlCamera2 = (RelativeLayout)findViewById(R.id.rl_camera2);
        rlCamera3 = (RelativeLayout)findViewById(R.id.rl_camera3);
        tvCamera2 = (TextView)findViewById(R.id.tv_camera2);
        tvCameraShare = (TextView)findViewById(R.id.tv_camera_share);
        tvCameraReset = (TextView)findViewById(R.id.tv_camera_reset);
        tvCameraSave = (TextView)findViewById(R.id.tv_camera_save);
    }

    private void initData(){
        int type = getIntent().getIntExtra(Constant.KEY_PIC_CHOOSE_TYPE,-1);
        if(type == Constant.VALUE_PIC_CHOOSE_TYPE_OFFICIAL){
            int resId = getIntent().getIntExtra(Constant.KEY_PIC_CHOOSE_DATA,-1);
            if(resId == -1){
                showError();
            }
            ivExampleIcon.setImageResource(resId);
        }else if(type == Constant.VALUE_PIC_CHOOSE_TYPE_OWN){
            Uri url = getIntent().getParcelableExtra(Constant.KEY_PIC_CHOOSE_DATA);
            ivExampleIcon.setImageURI(url);
        }else{
            showError();
        }


        ivCameraTake.setOnClickListener(this);
        tvCameraReset.setOnClickListener(this);
        tvCameraSave.setOnClickListener(this);
        tvCameraShare.setOnClickListener(this);


    }

    private void initSurfaceView(){
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //获取camera对象
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
                        Camera.CameraInfo info = new Camera.CameraInfo();
                        Camera.getCameraInfo(i, info);
                        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                            camera = Camera.open(i);
                        }
                    }
                }
                if (camera == null) {
                    camera = Camera.open();
                }

                try {
                    //设置预览监听
                    camera.setPreviewDisplay(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                    camera.release();
                    System.out.println("camera.release");
                }
                Camera.Parameters parameters = camera.getParameters();

                if (CameraFaceActivity.this.getResources().getConfiguration().orientation
                        != Configuration.ORIENTATION_LANDSCAPE) {
                    parameters.set("orientation", "portrait");
                    camera.setDisplayOrientation(90);
                    parameters.setRotation(90);
                } else {
                    parameters.set("orientation", "landscape");
                    camera.setDisplayOrientation(0);
                    parameters.setRotation(0);
                }
                camera.setParameters(parameters);
                //启动摄像头预览
                camera.startPreview();


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                System.out.println("camera.startpreview");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (camera != null) {
                    camera.stopPreview();
                    camera.release();
                }
            }
        });
    }

    private void showError(){
        Toast.makeText(this,"出错啦~无法获取到范例图片",Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_camera_take:
                takePhoto();
                break;
            case R.id.tv_camera_save:
                savePhoto();
                break;
            case R.id.tv_camera_reset:
                resetPhoto();
                break;
            case R.id.tv_camera_share:
                sharePhoto();
                break;
        }
    }



    private void takePhoto(){
        rlCamera1.setVisibility(View.INVISIBLE);
        rlCamera2.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(3);
        valueAnimator.setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int num = (int) animation.getAnimatedValue();
                if (originalNum != num) {
                    tvCamera2.setText(String.valueOf(3 - num));
                }
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        rlCamera2.setVisibility(View.INVISIBLE);
                        rlCamera3.setVisibility(View.VISIBLE);
                        photoData = data;
                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }


    private void savePhoto(){
        new SavePictureTask().execute(photoData);
        Toast.makeText(this,"已保存到手机",Toast.LENGTH_LONG).show();
        resetPhoto();
    }

    private void resetPhoto(){
        rlCamera3.setVisibility(View.INVISIBLE);
        rlCamera1.setVisibility(View.VISIBLE);
        camera.startPreview();
    }

    private void sharePhoto(){
        new SavePictureTask().execute(photoData);
        WXShareManager.ShareContentPic shareContentPic = manager.new ShareContentPic(photoData);
        manager.shareByWeixin(shareContentPic,WXShareManager.WEIXIN_SHARE_TYPE_TALK);
    }

    class SavePictureTask extends AsyncTask<byte[], String, String> {
        @Override
        protected String doInBackground(byte[]... params) {
            UUID uuid = UUID.randomUUID();
            String picName = uuid.toString();
            File picture = new File("/sdcard/"+picName+".jpg");
            try {
                FileOutputStream fos = new FileOutputStream(picture.getPath());
                fos.write(params[0]);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("", "照片保存完成");
            return null;
        }
    }


}
