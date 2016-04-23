package com.seriousface.m.myapplication.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seriousface.m.myapplication.R;
import com.seriousface.m.myapplication.constant.Constant;
import com.seriousface.m.myapplication.wxapi.WXShareManager;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2016/4/15.
 */
public class CameraFaceActivity extends BaseActivity implements View.OnClickListener{

    int screenWidth;
    int screenHeight;

    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera camera;
    byte[] photoData;
    Bitmap shardBitmap;
    ImageView ivCameraTake;
    ImageView ivExampleIcon;
    UMShareAPI mShareAPI;
    SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
            {
                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
            };

    RelativeLayout rlCamera1;
    RelativeLayout rlCamera3;
    ImageView tvCameraSave;
    ImageView tvCameraReset;
    RelativeLayout tvCameraShare;

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
        initShare();

        DisplayMetrics  dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //窗口的宽度
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    private void initView(){
        surfaceView = (SurfaceView)findViewById(R.id.sv_camera);
        ivCameraTake = (ImageView)findViewById(R.id.iv_camera_take);
        ivExampleIcon = (ImageView)findViewById(R.id.iv_compare_icon);

        rlCamera1 = (RelativeLayout)findViewById(R.id.rl_camera1);
        rlCamera3 = (RelativeLayout)findViewById(R.id.rl_camera3);
        tvCameraShare = (RelativeLayout)findViewById(R.id.tv_camera_share);
        tvCameraReset = (ImageView)findViewById(R.id.tv_camera_reset);
        tvCameraSave = (ImageView)findViewById(R.id.tv_camera_save);
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

        ivCameraTake.setImageResource(R.drawable.icon_camera_3);

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

    private void initShare(){
        mShareAPI = UMShareAPI.get(this);
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
        ValueAnimator valueAnimator = ValueAnimator.ofInt(3);
        valueAnimator.setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int num = (int) animation.getAnimatedValue();
                if (originalNum != num) {
                    if(3 - num==2){
                        ivCameraTake.setImageResource(R.drawable.icon_camera_2);
                    }else if(3 - num==1){
                        ivCameraTake.setImageResource(R.drawable.icon_camera_1);
                    }
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
                        rlCamera1.setVisibility(View.INVISIBLE);
                        rlCamera3.setVisibility(View.VISIBLE);
                        photoData = data;
                        createFinalPhoto();
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
        writePhotoToPhone();
        Toast.makeText(this,"已保存到手机",Toast.LENGTH_LONG).show();
        resetPhoto();
    }

    private void resetPhoto(){
        rlCamera3.setVisibility(View.INVISIBLE);
        ivCameraTake.setImageResource(R.drawable.icon_camera_3);
        rlCamera1.setVisibility(View.VISIBLE);
        camera.startPreview();
    }

    private void sharePhoto(){
        writePhotoToPhone();
        UMImage image = new UMImage(this, shardBitmap);

        new ShareAction(this).setDisplayList(displaylist)
                .withText(" ")
                .withMedia(image)
                .setListenerList(new ShardListener())
                .open();

//        WXShareManager.ShareContentPic shareContentPic = manager.new ShareContentPic(shardBitmap);
//        manager.shareByWeixin(shareContentPic, WXShareManager.WEIXIN_SHARE_TYPE_TALK);
    }

    private void writePhotoToPhone(){
        //旋转180
//        Bitmap bitmap = BitmapFactory.decodeByteArray(photoData, 0, photoData.length);
        //adjustPhotoRotation(bitmap, 180);
        //转成byte[]
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

//        photoData = baos.toByteArray();

        new SavePictureTask().execute(photoData);
    }

    private class ShardListener implements UMShareListener{

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Toast.makeText(CameraFaceActivity.this,"分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Toast.makeText(CameraFaceActivity.this,"分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Toast.makeText(CameraFaceActivity.this,"分享取消啦", Toast.LENGTH_SHORT).show();
        }
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
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("", "照片保存完成");
            return null;
        }
    }

    private void createFinalPhoto(){
        final View view = LayoutInflater.from(this).inflate(R.layout.layout_result_photo_officail1,null);
        ImageView ivMe = (ImageView)view.findViewById(R.id.iv_camera_final_photo_official1);
        ImageView ivExample = (ImageView)view.findViewById(R.id.iv_camera_final_photo_example);
//        view.measure(screenWidth, screenHeight - getResources().getDimensionPixelSize(R.dimen.camera_bottom_content_height));
        Bitmap bmp = BitmapFactory.decodeByteArray(photoData, 0, photoData.length);
        ivMe.setImageBitmap(adjustPhotoRotation(bmp,-90));

        ivExample.setImageResource(R.drawable.icon_test);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth,screenHeight - getResources().getDimensionPixelSize(R.dimen.camera_bottom_content_height));
        view.layout(0, 0, screenWidth, screenHeight - getResources().getDimensionPixelSize(R.dimen.camera_bottom_content_height));
        view.setLayoutParams(params);

        shardBitmap = loadBitmapFromView(view);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        shardBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        photoData = bao.toByteArray();


    }



    /**
     * 将view转成bitmap
     *
     * @param view
     * @return
     */
    public Bitmap loadBitmapFromView(View view) {
        if (view == null) {
            return null;
        }
        view.measure(View.MeasureSpec.makeMeasureSpec(screenWidth,View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(screenHeight - getResources().getDimensionPixelSize(R.dimen.camera_bottom_content_height), View.MeasureSpec.EXACTLY));
        // 这个方法也非常重要，设置布局的尺寸和位置
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        // 生成bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        // 利用bitmap生成画布
        Canvas canvas = new Canvas(bitmap);
        // 把view中的内容绘制在画布上
        view.draw(canvas);

        return bitmap;
    }


    public Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree)
    {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        m.postScale(-1, 1);
        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            Log.d("turnphoto","yes");
            return bm1;
        } catch (OutOfMemoryError ex) {
        }
        Log.d("turnphoto","no");
        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
        resetPhoto();
    }
}
