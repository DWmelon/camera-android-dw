package com.seriousface.m.myapplication.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seriousface.m.myapplication.R;
import com.seriousface.m.myapplication.constant.Constant;
import com.seriousface.m.myapplication.constant.StatConstant;
import com.seriousface.m.myapplication.util.CamParaUtil;
import com.seriousface.m.myapplication.util.DisplayUtil;
import com.seriousface.m.myapplication.wxapi.WXShareManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
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
    float previewRate = -1f;
    byte[] photoData;
    Bitmap shardBitmap;
    TextView ivCameraTake;
    ImageView ivExampleIcon;
    UMShareAPI mShareAPI;
    SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
            {
                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA,
                    SHARE_MEDIA.QQ
            };

    ImageView ivCameraTurn;
    RelativeLayout rlCamera1;
    RelativeLayout rlCamera3;
    ImageView tvCameraSave;
    ImageView tvCameraReset;
    TextView ivBack;
    RelativeLayout tvCameraShare;

    int pageType;
    int imgResId;
    Uri imgUrl;
//    int cameraPosition = 0;
    int originalNum = -1;
    WXShareManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_face);
        manager = WXShareManager.getInstance(this);
        initView();
        initViewParams();
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

    private void initViewParams(){
        ViewGroup.LayoutParams params = surfaceView.getLayoutParams();
        Point p = DisplayUtil.getScreenMetrics(this);
        params.width = p.x;
        params.height = p.y;
        previewRate = DisplayUtil.getScreenRate(this); //默认全屏的比例预览
        surfaceView.setLayoutParams(params);

    }

    private void initData(){
        pageType = getIntent().getIntExtra(Constant.KEY_PIC_CHOOSE_TYPE, -1);
        if(pageType == Constant.VALUE_PIC_CHOOSE_TYPE_OFFICIAL){
            imgResId = getIntent().getIntExtra(Constant.KEY_PIC_CHOOSE_DATA,-1);
            if(imgResId == -1){
                showError();
            }
            ivExampleIcon.setImageResource(imgResId);
        }else if(pageType == Constant.VALUE_PIC_CHOOSE_TYPE_OWN){
            imgUrl = getIntent().getParcelableExtra(Constant.KEY_PIC_CHOOSE_DATA);
            ivExampleIcon.setImageURI(imgUrl);
        }else{
            showError();
        }

        ivCameraTake.setBackgroundResource(R.drawable.icon_camera_btn_gray);
        ivCameraTake.setText("3");

        ivCameraTake.setOnClickListener(this);
        ivCameraTurn.setOnClickListener(this);
        tvCameraReset.setOnClickListener(this);
        tvCameraSave.setOnClickListener(this);
        tvCameraShare.setOnClickListener(this);
        ivBack.setOnClickListener(this);

    }

    private void initSurfaceView(){
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //获取camera对象
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
//                    for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
//                        Camera.CameraInfo info = new Camera.CameraInfo();
//                        Camera.getCameraInfo(i, info);
//                        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                            camera = Camera.open(i);
//                        }
//                    }
//                    if(cameraPosition == 1){
//                        cameraPosition = 0;
//                    }else{
//                        cameraPosition = 1;
//                    }
                        turnPhoto();

                }
//                if (camera == null) {
//                    camera = Camera.open();
//                }

//                try {
//                    //设置预览监听
//                    camera.setPreviewDisplay(holder);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    camera.release();
//                    camera=null;
//                    System.out.println("camera.release");
//                }
//                Camera.Parameters parameters = camera.getParameters();


//                parameters = camera.getParameters();
//                parameters.setPictureFormat(PixelFormat.JPEG);//设置拍照后存储的图片格式
//                CamParaUtil.getInstance().printSupportPictureSize(parameters);
//                CamParaUtil.getInstance().printSupportPreviewSize(parameters);
//                //设置PreviewSize和PictureSize
//                Camera.Size pictureSize = CamParaUtil.getInstance().getPropPictureSize(
//                        parameters.getSupportedPictureSizes(), previewRate, 800);
//                parameters.setPictureSize(pictureSize.width, pictureSize.height);
//                Camera.Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(
//                        parameters.getSupportedPreviewSizes(), previewRate, 800);
//                parameters.setPreviewSize(previewSize.width, previewSize.height);
//
//                camera.setDisplayOrientation(90);
//
//                CamParaUtil.getInstance().printSupportFocusMode(parameters);
//                List<String> focusModes = parameters.getSupportedFocusModes();
//                if (focusModes.contains("continuous-video")) {
//                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
//                }
//                camera.setParameters(parameters);
//
//                //启动摄像头预览
//                camera.startPreview();


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
                    camera=null;
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
            case R.id.tv_camera_take:
                takePhoto();
                MobclickAgent.onEvent(CameraFaceActivity.this, StatConstant.BtnTakePhoto);
                break;
            case R.id.tv_camera_save:
                savePhoto();
                MobclickAgent.onEvent(CameraFaceActivity.this, StatConstant.BtnSavePhoto);
                break;
            case R.id.tv_camera_reset:
                resetPhoto();
                break;
            case R.id.tv_camera_share:
                sharePhoto();
                MobclickAgent.onEvent(CameraFaceActivity.this, StatConstant.BtnSharePhoto);
                break;
            case R.id.iv_turn_photo:
                turnPhoto();
                MobclickAgent.onEvent(CameraFaceActivity.this, StatConstant.BtnTurnPhoto);
                break;
            case R.id.tv_camera_back:
                finish();
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
                    if (3 - num == 3) {
                        ivCameraTake.setBackgroundResource(R.drawable.btn_take_photo_yellow);
                    } else if (3 - num == 2) {
                        ivCameraTake.setText("2");
                    } else if (3 - num == 1) {
                        ivCameraTake.setText("1");
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
                if (camera != null) {
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
        ivCameraTake.setBackgroundResource(R.drawable.icon_camera_btn_gray);
        ivCameraTake.setText("3");
        rlCamera1.setVisibility(View.VISIBLE);

        if(camera!=null){
            camera.startPreview();
        }

//        if(cameraPosition == 0){
//            cameraPosition = 1;
//        }else{
//            cameraPosition = 0;
//        }
//
//        turnPhoto();

    }

    private void sharePhoto(){
        writePhotoToPhone();
        UMImage image = new UMImage(this, shardBitmap);

        new ShareAction(this).setDisplayList(displaylist)
                .withMedia(image)
                .setListenerList(new ShardListener())
                .open();

//        WXShareManager.ShareContentPic shareContentPic = manager.new ShareContentPic(shardBitmap);
//        manager.shareByWeixin(shareContentPic, WXShareManager.WEIXIN_SHARE_TYPE_TALK);

    }

    private void turnPhoto(){
        //切换前后摄像头
        int cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

        //获得当前前置还是后置，拿不到就设置后置
        int NOW_POSITION = Camera.CameraInfo.CAMERA_FACING_BACK;


        if(camera!=null){
            camera.stopPreview();//停掉原来摄像头的预览
            camera.release();//释放资源
            camera = null;//取消原来摄像头
        }


        for(int i = 0; i < cameraCount;i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if(cameraInfo.facing  == NOW_POSITION) {

                //代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置

                camera = Camera.open();//打开当前选中的摄像头

                Camera.Parameters parameters = camera.getParameters();
                parameters.setPictureFormat(PixelFormat.JPEG);//设置拍照后存储的图片格式
                CamParaUtil.getInstance().printSupportPictureSize(parameters);
                CamParaUtil.getInstance().printSupportPreviewSize(parameters);
                //设置PreviewSize和PictureSize
                Camera.Size pictureSize = CamParaUtil.getInstance().getPropPictureSize(
                        parameters.getSupportedPictureSizes(), previewRate, 800);
                parameters.setPictureSize(pictureSize.width, pictureSize.height);
                Camera.Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(
                        parameters.getSupportedPreviewSizes(), previewRate, 800);
                parameters.setPreviewSize(previewSize.width, previewSize.height);

                camera.setDisplayOrientation(90);

                CamParaUtil.getInstance().printSupportFocusMode(parameters);
                List<String> focusModes = parameters.getSupportedFocusModes();
                if (focusModes.contains("continuous-video")) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                }
                camera.setParameters(parameters);

                try {
                    camera.setPreviewDisplay(surfaceHolder);//通过surfaceview显示取景画面
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                camera.startPreview();//开始预览
                break;
            }
            if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_BACK){
                //现在是前置， 变更为后置
                //代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                camera = Camera.open(i);//打开当前选中的摄像头

                Camera.Parameters parameters = camera.getParameters();
                parameters.setPictureFormat(PixelFormat.JPEG);//设置拍照后存储的图片格式
                CamParaUtil.getInstance().printSupportPictureSize(parameters);
                CamParaUtil.getInstance().printSupportPreviewSize(parameters);
                //设置PreviewSize和PictureSize
                Camera.Size pictureSize = CamParaUtil.getInstance().getPropPictureSize(
                        parameters.getSupportedPictureSizes(), previewRate, 800);
                parameters.setPictureSize(pictureSize.width, pictureSize.height);
                Camera.Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(
                        parameters.getSupportedPreviewSizes(), previewRate, 800);
                parameters.setPreviewSize(previewSize.width, previewSize.height);

                camera.setDisplayOrientation(90);

                CamParaUtil.getInstance().printSupportFocusMode(parameters);
                List<String> focusModes = parameters.getSupportedFocusModes();
                if (focusModes.contains("continuous-video")) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                }

                try {
                    camera.setPreviewDisplay(surfaceHolder);//通过surfaceview显示取景画面
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                camera.startPreview();//开始预览
                break;
            }

        }
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
            resetPhoto();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Toast.makeText(CameraFaceActivity.this,"分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
        }
    }

    class SavePictureTask extends AsyncTask<byte[], String, String> {
        @Override
        protected String doInBackground(byte[]... params) {

            File sdRoot = Environment.getExternalStorageDirectory();
            String dir = "/I'mbiaoqingbao/";
            File mkDir = new File(sdRoot, dir);
            if (!mkDir.exists())
            {
                mkDir.mkdirs();
            }


            UUID uuid = UUID.randomUUID();
            String picName = uuid.toString();
            File picture = new File(sdRoot,dir+picName+".jpg");
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
        final View view;
        if(pageType == Constant.VALUE_PIC_CHOOSE_TYPE_OFFICIAL){
            view = LayoutInflater.from(this).inflate(R.layout.layout_result_photo_officail1, (ViewGroup) (getWindow().getDecorView().findViewById(android.R.id.content)),false);
        }else{
            view = LayoutInflater.from(this).inflate(R.layout.layout_result_photo_officail1, (ViewGroup) (getWindow().getDecorView().findViewById(android.R.id.content)),false);
        }
        ImageView ivMe = (ImageView)view.findViewById(R.id.iv_camera_final_photo_official1);
        ImageView ivExample = (ImageView)view.findViewById(R.id.iv_camera_final_photo_example);
//        view.measure(screenWidth, screenHeight - getResources().getDimensionPixelSize(R.dimen.camera_bottom_content_height));
        Bitmap bmp = BitmapFactory.decodeByteArray(photoData, 0, photoData.length);
        ivMe.setImageBitmap(adjustPhotoRotation(bmp, 270));

        if(pageType == Constant.VALUE_PIC_CHOOSE_TYPE_OFFICIAL){
            ivExample.setImageResource(imgResId);
        }else{
            ivExample.setImageURI(imgUrl);
        }




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

        if(pageType == Constant.VALUE_PIC_CHOOSE_TYPE_OFFICIAL){

//            view.measure(View.MeasureSpec.makeMeasureSpec(getResources().getDimensionPixelSize(R.dimen.photo_type_width),View.MeasureSpec.EXACTLY),
//                    View.MeasureSpec.makeMeasureSpec(getResources().getDimensionPixelSize(R.dimen.photo_type_height), View.MeasureSpec.EXACTLY));
//            // 这个方法也非常重要，设置布局的尺寸和位置
//            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.measure(View.MeasureSpec.makeMeasureSpec(screenWidth,View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(screenHeight - getResources().getDimensionPixelSize(R.dimen.camera_bottom_content_height), View.MeasureSpec.EXACTLY));
            // 这个方法也非常重要，设置布局的尺寸和位置
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }else{
            view.measure(View.MeasureSpec.makeMeasureSpec(screenWidth,View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(screenHeight - getResources().getDimensionPixelSize(R.dimen.camera_bottom_content_height), View.MeasureSpec.EXACTLY));
            // 这个方法也非常重要，设置布局的尺寸和位置
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }


        // 生成bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.RGB_565);
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
            bm.recycle();
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



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(camera!=null){
            camera.stopPreview();
            camera.release();
            camera=null;
        }

    }
}
