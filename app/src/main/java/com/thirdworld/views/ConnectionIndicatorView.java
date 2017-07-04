package com.thirdworld.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import org.greenrobot.eventbus.Subscribe;

import szz.com.baselib.EventUtil;
import szz.com.baselib.entity.events.ConnectState;
import szz.com.thirdworld.R;

/**
 * 作者：Ying.Huang on 2017/6/22 17:08
 * Version v1.0
 * 描述：
 */

public class ConnectionIndicatorView extends android.support.v7.widget.AppCompatImageView {

    private int mCurState = 1;
    private boolean isConnected = false;

    public ConnectionIndicatorView(Context context) {
        this(context, null);
    }

    public ConnectionIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setSrc();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventUtil.register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventUtil.unregister(this);
    }

    @Subscribe
    public void changeConnectState(ConnectState state){
        isConnected = state.isConnected;
    }

    private void setSrc() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isShown()) {
                    if (isConnected) {
                        switch (mCurState++ % 2) {
                            case 0:
                                setImageResource(R.drawable.green);
                                break;
                            case 1:
                                setImageResource(R.drawable.green2);
                                break;
                        }
                    } else {
                        setImageResource(R.drawable.red);
                    }
                }
                setSrc();
            }
        }, 1000);
    }
}
