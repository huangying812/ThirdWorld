/**
 * description :
 * Created by csq E-mail:csqwyyx@163.com
 * 2016/8/18
 * Created with Studio
 */

package com.thirdworld.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;

public class ContextHolder {

    // ------------------------ Constants ------------------------


    // ------------------------- Fields --------------------------

    static Context ApplicationContext;

    // ----------------------- Constructors ----------------------


    // -------- Methods for/from SuperClass/Interfaces -----------


    // --------------------- Methods public ----------------------

    /**
     * 初始化context，如果由于不同机型导致反射获取context失败可以在Application调用此方法
     * @param context
     */
    public static void init(Context context) {
        ApplicationContext = context;
    }

    public static Context getContext() {
        if (ApplicationContext == null) {
            try {
                Application application = (Application) Class.forName("android.app.ActivityThread")
                        .getMethod("currentApplication").invoke(null, (Object[]) null);
                if (application != null) {
                    ApplicationContext = application;
                    return application;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Application application = (Application) Class.forName("android.app.AppGlobals")
                        .getMethod("getInitialApplication").invoke(null, (Object[]) null);
                if (application != null) {
                    ApplicationContext = application;
                    return application;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new IllegalStateException("ContextHolder is not initialed, it is recommend to init with application context.");
        }
        return ApplicationContext;
    }

    public static Activity getActivity(Context context) {
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }
        return null;
    }

    public static String getString(int resId) {
        return getContext().getString(resId);
    }

    public static String getString(int resId,Object... args) {
        String str = getString(resId);
        return getFormat(str, args);
    }

    public static String getFormat(String str, Object... args) {
        return String.format(str,args);
    }

    // --------------------- Methods private ---------------------


    // --------------------- Getter & Setter -----------------


    // --------------- Inner and Anonymous Classes ---------------


    // --------------------- logical fragments -----------------


}