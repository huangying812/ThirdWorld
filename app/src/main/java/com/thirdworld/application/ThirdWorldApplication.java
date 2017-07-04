package com.thirdworld.application;

import android.app.Application;
import android.content.Context;

import szz.com.baselib.EventUtil;
import szz.com.baselib.application.ContextHolder;
import szz.com.baselib.singleton.ConnectManager;

/**
 * 作者：Ying.Huang on 2017/6/14 09:10
 * Version v1.0
 * 描述：
 */

public class ThirdWorldApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ContextHolder.init(base);
        EventUtil.init();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ConnectManager.getInstance().release();
    }
}
