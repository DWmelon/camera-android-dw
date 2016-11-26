package com.seriousface.m.myapplication.listener;

import android.annotation.TargetApi;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupWindow;

import com.seriousface.m.myapplication.callback.PreviewSessionCallback;
import com.seriousface.m.myapplication.fragment.Camera2FaceFragment;
import com.seriousface.m.myapplication.view.AnimationTextView;

/**
 * Created by yuyidong on 14-12-23.
 */
@TargetApi(21)
public class SenseItemClickListener implements AdapterView.OnItemClickListener {
    private CaptureRequest.Builder mPreviewBuilder;
    private CameraCaptureSession mCameraCaptureSession;
    private Handler mHandler;
    private PopupWindow mWindow;
    private PreviewSessionCallback mPreviewSessionCallback;
    private AnimationTextView mAnimationTextView;


    public SenseItemClickListener(CaptureRequest.Builder mPreviewBuilder, CameraCaptureSession mCameraCaptureSession, Handler mHandler, PopupWindow mWindow, PreviewSessionCallback mPreviewSessionCallback, AnimationTextView mAnimationTextView) {
        this.mPreviewBuilder = mPreviewBuilder;
        this.mCameraCaptureSession = mCameraCaptureSession;
        this.mHandler = mHandler;
        this.mWindow = mWindow;
        this.mPreviewSessionCallback = mPreviewSessionCallback;
        this.mAnimationTextView = mAnimationTextView;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mPreviewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_USE_SCENE_MODE);
        switch (position) {
            case 0:
                mPreviewBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, CameraMetadata.CONTROL_SCENE_MODE_DISABLED);
                mAnimationTextView.start("DISABLED", Camera2FaceFragment.WINDOW_TEXT_DISAPPEAR);
                break;
            case 1:
                mPreviewBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, CameraMetadata.CONTROL_SCENE_MODE_FACE_PRIORITY);
                mAnimationTextView.start("FACE_PRIORITY", Camera2FaceFragment.WINDOW_TEXT_DISAPPEAR);
                break;
            case 2:
                mPreviewBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, CameraMetadata.CONTROL_SCENE_MODE_ACTION);
                mAnimationTextView.start("ACTION", Camera2FaceFragment.WINDOW_TEXT_DISAPPEAR);
                break;
            case 3:
                mPreviewBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, CameraMetadata.CONTROL_SCENE_MODE_PORTRAIT);
                mAnimationTextView.start("PORTRAIT", Camera2FaceFragment.WINDOW_TEXT_DISAPPEAR);
                break;
            case 4:
                mPreviewBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, CameraMetadata.CONTROL_SCENE_MODE_LANDSCAPE);
                mAnimationTextView.start("LANDSCAPE", Camera2FaceFragment.WINDOW_TEXT_DISAPPEAR);
                break;
            case 5:
                mPreviewBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, CameraMetadata.CONTROL_SCENE_MODE_NIGHT);
                mAnimationTextView.start("NIGHT", Camera2FaceFragment.WINDOW_TEXT_DISAPPEAR);
                break;
            case 6:
                mPreviewBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, CameraMetadata.CONTROL_SCENE_MODE_NIGHT_PORTRAIT);
                mAnimationTextView.start("PORTRAIT", Camera2FaceFragment.WINDOW_TEXT_DISAPPEAR);
                break;
            case 7:
                mPreviewBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, CameraMetadata.CONTROL_SCENE_MODE_THEATRE);
                mAnimationTextView.start("THEATRE", Camera2FaceFragment.WINDOW_TEXT_DISAPPEAR);
                break;
            case 8:
                mPreviewBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, CameraMetadata.CONTROL_SCENE_MODE_BEACH);
                mAnimationTextView.start("BEACH", Camera2FaceFragment.WINDOW_TEXT_DISAPPEAR);
                break;
            case 9:
                mPreviewBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, CameraMetadata.CONTROL_SCENE_MODE_SNOW);
                mAnimationTextView.start("SNOW", Camera2FaceFragment.WINDOW_TEXT_DISAPPEAR);
                break;
            case 10:
                mPreviewBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, CameraMetadata.CONTROL_SCENE_MODE_SUNSET);
                mAnimationTextView.start("SUNSET", Camera2FaceFragment.WINDOW_TEXT_DISAPPEAR);
                break;
            case 11:
                mPreviewBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, CameraMetadata.CONTROL_SCENE_MODE_STEADYPHOTO);
                mAnimationTextView.start("STEADYPHOTO", Camera2FaceFragment.WINDOW_TEXT_DISAPPEAR);
                break;
            case 12:
                mPreviewBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, CameraMetadata.CONTROL_SCENE_MODE_FIREWORKS);
                mAnimationTextView.start("FIREWORKS", Camera2FaceFragment.WINDOW_TEXT_DISAPPEAR);
                break;
            case 13:
                mPreviewBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, CameraMetadata.CONTROL_SCENE_MODE_SPORTS);
                mAnimationTextView.start("SPORTS", Camera2FaceFragment.WINDOW_TEXT_DISAPPEAR);
                break;
            case 14:
                mPreviewBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, CameraMetadata.CONTROL_SCENE_MODE_PARTY);
                mAnimationTextView.start("PARTY", Camera2FaceFragment.WINDOW_TEXT_DISAPPEAR);
                break;
            case 15:
                mPreviewBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, CameraMetadata.CONTROL_SCENE_MODE_CANDLELIGHT);
                mAnimationTextView.start("CANDLELIGHT", Camera2FaceFragment.WINDOW_TEXT_DISAPPEAR);
                break;
            case 16:
                mPreviewBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, CameraMetadata.CONTROL_SCENE_MODE_BARCODE);
                mAnimationTextView.start("BARCODE", Camera2FaceFragment.WINDOW_TEXT_DISAPPEAR);
                break;
        }
        updatePreview();
        mWindow.dismiss();
    }

    /**
     * 更新预览
     */
    private void updatePreview() {
        try {
            mCameraCaptureSession.setRepeatingRequest(mPreviewBuilder.build(), mPreviewSessionCallback, mHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("updatePreview", "ExceptionExceptionException");
        }
    }
}
