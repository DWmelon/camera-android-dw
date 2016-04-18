package com.seriousface.m.myapplication.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.res.Configuration;
import android.hardware.Camera;
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
import com.seriousface.m.myapplication.application.MyApplication;
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
    SurfaceHolder sfHolder;
    Camera camera;
    byte[] photoData;
    ImageView ivCameraTake;
    ImageView ivCameraTurn;

    RelativeLayout rlCamera1;
    RelativeLayout rlCamera2;
    RelativeLayout rlCamera3;
    TextView tvCamera2;
    TextView tvCameraSave;
    TextView tvCameraReset;
    TextView tvCameraShare;

    boolean isFrontCamera = false;
    int orginalNum = -1;
    WXShareManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_face);

        manager = WXShareManager.getInstance(this);

        surfaceView = (SurfaceView)findViewById(R.id.sv_camera);
        ivCameraTake = (ImageView)findViewById(R.id.iv_camera_take);
        ivCameraTurn = (ImageView)findViewById(R.id.iv_camera_turn);

        rlCamera1 = (RelativeLayout)findViewById(R.id.rl_camera1);
        rlCamera2 = (RelativeLayout)findViewById(R.id.rl_camera2);
        rlCamera3 = (RelativeLayout)findViewById(R.id.rl_camera3);
        tvCamera2 = (TextView)findViewById(R.id.tv_camera2);
        tvCameraShare = (TextView)findViewById(R.id.tv_camera_share);
        tvCameraReset = (TextView)findViewById(R.id.tv_camera_reset);
        tvCameraSave = (TextView)findViewById(R.id.tv_camera_save);

        ivCameraTake.setOnClickListener(this);
        ivCameraTurn.setOnClickListener(this);
        tvCameraReset.setOnClickListener(this);
        tvCameraSave.setOnClickListener(this);
        tvCameraShare.setOnClickListener(this);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                sfHolder = holder;
                turnPhoto();


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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_camera_take:
                takePhoto();
                break;
            case R.id.iv_camera_turn:
                turnPhoto();
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
                if (orginalNum != num) {
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

    private void turnPhoto(){
        if(camera!=null){
            camera.release();
            camera = null;
        }
        if(isFrontCamera){
            camera = Camera.open();
            isFrontCamera = false;
        }else{
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
                isFrontCamera = false;
            }else{
                isFrontCamera = true;
            }

        }

        try {
            //设置预览监听
            camera.setPreviewDisplay(sfHolder);
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

    private void savePhoto(){
        new SavePictureTask().execute(photoData);
        Toast.makeText(this,"已保存到手机",Toast.LENGTH_LONG).show();
        resetPhoto();
    }

    private void resetPhoto(){
        if(isFrontCamera){
            isFrontCamera = false;
        }else{
            isFrontCamera = true;
        }
        rlCamera3.setVisibility(View.INVISIBLE);
        rlCamera1.setVisibility(View.VISIBLE);
        turnPhoto();
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
