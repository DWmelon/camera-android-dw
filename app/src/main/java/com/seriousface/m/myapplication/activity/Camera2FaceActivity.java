package com.seriousface.m.myapplication.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.seriousface.m.myapplication.PreferenceHelper;
import com.seriousface.m.myapplication.R;
import com.seriousface.m.myapplication.fragment.Camera2FaceFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melon on 2016/11/1.
 */
@TargetApi(21)
public class Camera2FaceActivity extends BaseActivity{

    private static final int INIT_OK = 1;
    private static final int INIT_BAD = 2;
    private Dialog mDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INIT_OK:
                    closeDialogLoading();
                    Camera2FaceFragment camera2FaceFragment = Camera2FaceFragment.newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putString("cameraid", PreferenceHelper.getCurrentCameraid(Camera2FaceActivity.this));
                    camera2FaceFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.fl_contain, camera2FaceFragment).commit();
                    break;
                case INIT_BAD:
                    closeDialogLoading();
                    Toast.makeText(Camera2FaceActivity.this, "初始化失败！！！", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_face2);
        //第一次初始化
        if (!PreferenceHelper.checkFirstInit(this)) {
            initDialogLoading();
            new Thread(new MyRunnable()).start();
        } else {
            Camera2FaceFragment camera2FaceFragment = Camera2FaceFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString("cameraid", PreferenceHelper.getCurrentCameraid(this));
            camera2FaceFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.fl_contain, camera2FaceFragment).commit();
        }
    }

    /**
     * 再子线程中初始化相机参数
     */
    class MyRunnable implements Runnable {

        @SuppressWarnings("ResourceType")
        @Override
        public void run() {
            try {
                //初始化参数到sharedPreference
                CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                String[] cameraIds = manager.getCameraIdList();
                if (cameraIds != null && cameraIds.length > 0) {
                    //后置摄像头存在
                    if (cameraIds[0] != null) {
                        CameraCharacteristics character = manager.getCameraCharacteristics(cameraIds[0]);
                        //流配置
                        StreamConfigurationMap map = character.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                        //适合SurfaceTexture的显示的size
                        Size[] previewSizes = map.getOutputSizes(SurfaceTexture.class);
                        //图片格式
                        int[] formatsAll = map.getOutputFormats();
                        //这里只要jpeg和dng
                        List<Integer> formatList = new ArrayList<Integer>();
                        for (int format : formatsAll) {
                            if (format == ImageFormat.JPEG || format == ImageFormat.RAW_SENSOR) {
                                formatList.add(format);
                            }
                        }
                        Integer[] formats = formatList.toArray(new Integer[formatList.size()]);
                        //不同的format对应不同的照片size
                        Size[][] pictureSizes = new Size[formats.length][];
                        for (int i = 0; i < formats.length; i++) {
                            //这里会出现有的格式但是没有保存图片的size
                            if (null != map.getOutputSizes(formats[i])) {
                                pictureSizes[i] = map.getOutputSizes(formats[i]);
                            } else {
                                Log.i("Runnable", "camera0--->map.getOutputSizes为空");
                            }
                        }
                        PreferenceHelper.writePreferenceForCameraId(Camera2FaceActivity.this, "camera0", previewSizes, formats, pictureSizes);

                    }
                    if (cameraIds[1] != null) {//前置摄像头存在
                        CameraCharacteristics character = manager.getCameraCharacteristics(cameraIds[1]);
                        //流配置
                        StreamConfigurationMap map = character.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                        //适合SurfaceTexture的显示的size
                        Size[] previewSizes = map.getOutputSizes(SurfaceTexture.class);
                        //图片格式
                        int[] formatsAll = map.getOutputFormats();
                        //这里只要jpeg和dng
                        List<Integer> formatList = new ArrayList<Integer>();
                        for (int format : formatsAll) {
                            if (format == ImageFormat.JPEG || format == ImageFormat.RAW_SENSOR) {
                                formatList.add(format);
                            }
                        }
                        Integer[] formats = formatList.toArray(new Integer[formatList.size()]);
                        //不同的format对应不同的照片size
                        Size[][] pictureSizes = new Size[formats.length][];
                        for (int i = 0; i < formats.length; i++) {
                            //这里会出现有的格式但是没有保存图片的size
                            if (null != map.getOutputSizes(formats[i])) {
                                pictureSizes[i] = map.getOutputSizes(formats[i]);
                            } else {
                                Log.i("Runnable", "camera1--->map.getOutputSizes为空");
                            }
                        }
                        PreferenceHelper.writePreferenceForCameraId(Camera2FaceActivity.this, "camera1", previewSizes, formats, pictureSizes);
                    }
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(INIT_BAD);
                return;
            }
            //初始化一开始打开cameraia为1的摄像头
            PreferenceHelper.writeCurrentCameraid(Camera2FaceActivity.this, "0");
            mHandler.sendEmptyMessage(INIT_OK);
        }
    }

    /**
     * 初始化&&显示dialog
     */
    private void initDialogLoading() {
        View v = getLayoutInflater().inflate(R.layout.loading_dialog, null);
        ImageView imageView = (ImageView) v.findViewById(R.id.img_loading);
        LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.liner_dialog);
        Animation animation = AnimationUtils.loadAnimation(Camera2FaceActivity.this, R.anim.dialog_loading);
        imageView.setAnimation(animation);
        mDialog = new Dialog(Camera2FaceActivity.this, R.style.loading_dialog);
        mDialog.setCancelable(false);
        mDialog.setContentView(linearLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    /**
     * 关闭dialog
     */
    private void closeDialogLoading() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
