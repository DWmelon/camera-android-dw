package com.seriousface.m.myapplication.application;

import android.app.Application;

import com.seriousface.m.myapplication.wxapi.WXShareManager;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.PlatformConfig;

import java.util.Objects;

/**
 * Created by Administrator on 2016/4/15.
 */
public class MyApplication extends Application {

    private static MyApplication myApplication;

    public static MyApplication getInstants(){
        if(myApplication == null){
            myApplication = new MyApplication();
            init();
        }
        return myApplication;
    }


    @Override
    public void onCreate() {
        super.onCreate();
		Fresco.initialize(this);
        myApplication = getInstants();
		
    }

    private static void init(){
        PlatformConfig.setWeixin("wx81fd52b3abdc249f", "4a1f0f218cf63acef71893726f7a19c3");
        //微信 appid appsecret
        PlatformConfig.setSinaWeibo("1289740435","2ba9244d9cb46a501f29cefa081f40cf");
        //新浪微博 appkey appsecret
        PlatformConfig.setQQZone("1105343676", "PlYBLtkEJ01sydmT");
    }




}
