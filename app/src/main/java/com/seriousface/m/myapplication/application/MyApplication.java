package com.seriousface.m.myapplication.application;

import android.app.Application;

import com.seriousface.m.myapplication.wxapi.WXShareManager;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Objects;

/**
 * Created by Administrator on 2016/4/15.
 */
public class MyApplication extends Application {

    private static MyApplication myApplication;

    public static MyApplication getInstants(){
        if(myApplication == null){
            myApplication = new MyApplication();
        }
        return myApplication;
    }


    @Override
    public void onCreate() {
        super.onCreate();

    }






}
