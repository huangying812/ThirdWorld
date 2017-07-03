package com.thirdworld.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.thirdworld.EventUtil;
import com.thirdworld.application.ContextHolder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import szz.com.baselib.entity.events.LoginSuccess;

/**
 * 作者：Ying.Huang on 2017/6/16 13:23
 * Version v1.0
 * 描述：
 */

public abstract class BaseActivity extends AppCompatActivity{

    public static final int WHAT_SERVER_RESP = 0x11;
    public static final int WHAT_ERROR = 0x12;
    public static final int WHAT_COMM = 0x13;
    protected BaseActivity self;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        EventUtil.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventUtil.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void login(LoginSuccess event){
        onResume();
    }

    /**
     * @param title
     * @param message
     * @param negativeListener
     * @param positiveListener
     */
    protected void showDialog(String title, String message, View view, DialogInterface.OnClickListener negativeListener, DialogInterface.OnClickListener positiveListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("取消", negativeListener);
        builder.setPositiveButton("确定", positiveListener);
        builder.setView(view);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder.show();
            }
        });
    }

    /**
     * @param title
     * @param message
     * @param view
     * @param positiveListener
     */
    protected void showDialog(String title, String message, View view, DialogInterface.OnClickListener positiveListener) {
        showDialog(title, message, view, null, positiveListener);
    }

    public static String getContentStr(@NonNull TextView view) {
        return view.getText().toString().trim();
    }

    public static int getContentInt(@NonNull TextView view) {
        int result = 0;
        try {
            result = Integer.valueOf(getContentStr(view));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    protected String getFormat(String str, Object... args) {
        return ContextHolder.getFormat(str, args);
    }

    protected String getFormat(int resId, Object... args) {
        return getFormat(getString(resId), args);
    }

    protected void showToast(@StringRes int resId) {
        showToast(getString(resId));
    }

    protected void showToast(final String res) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(ContextHolder.getContext(), res, Toast.LENGTH_SHORT).show();
                Log.d(getClass().getSimpleName(), "run: "+res);
            }
        });
    }
}
