/**
 * description :
 * Created by csq E-mail:csqwyyx@163.com
 * 2016/4/22
 * Created with Studio
 */

package com.thirdworld.application;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 不支持多进程！！！
 */
public class SharedPreferenceUtil {

    private static HashMap<String, LinkedHashMap<String, Integer>> cachedIntValues = new HashMap<>();
    private static HashMap<String, LinkedHashMap<String, Long>> cachedLongValues = new HashMap<>();
    private static HashMap<String, LinkedHashMap<String, Boolean>> cachedBooleanValues = new HashMap<>();
    private static HashMap<String, LinkedHashMap<String, Float>> cachedFloatValues = new HashMap<>();

    //====================== int ==========================
    /**
     * 通用int的保存读取
     */
    public static boolean saveInt(Context context, String key, int value, String preferenceName) {
        return saveInt(context, key, value, preferenceName, true);
    }

    public static boolean saveInt(Context context, String key, int value, String preferenceName, boolean cache) {
        SharedPreferences prefs = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (editor != null) {
            editor.putInt(key, value);
            editor.apply();

            if(cache){
                LinkedHashMap<String, Integer> cachedValues = cachedIntValues.get(preferenceName);
                if(cachedValues == null){
                    cachedValues = new LinkedHashMap<>();
                    cachedIntValues.put(preferenceName, cachedValues);
                }

                cachedValues.put(key, value);
                if(cachedValues.size() > 30){
                    String first = cachedValues.entrySet().iterator().next().getKey();
                    cachedValues.remove(first);
                }
            }
            return true;
        }
        return false;
    }

    public static int getInt(Context context, String key, int defValue, String preferenceName) {
        return getInt(context, key, defValue, preferenceName, true);
    }

    public static int getInt(Context context, String key, int defValue, String preferenceName, boolean useCache) {
        if (useCache) {
            LinkedHashMap<String, Integer> cachedValues = cachedIntValues.get(preferenceName);
            if(cachedValues != null){
                Integer cache = cachedValues.get(key);
                if(cache != null){
                    return cache;
                }
            }
        }

        SharedPreferences prefs = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        return prefs.getInt(key, defValue);
    }


    //====================== long ==========================

    public static boolean saveLong(Context context, String key, long value, String preferenceName) {
        return saveLong(context, key, value, preferenceName, true);
    }

    /**
     * 通用long的保存读取
     */
    public static boolean saveLong(Context context, String key, long value, String preferenceName, boolean cache) {
        SharedPreferences prefs = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (editor != null) {
            editor.putLong(key, value);
            editor.apply();

            if(cache){
                LinkedHashMap<String, Long> cachedValues = cachedLongValues.get(preferenceName);
                if(cachedValues == null){
                    cachedValues = new LinkedHashMap<>();
                    cachedLongValues.put(preferenceName, cachedValues);
                }

                cachedValues.put(key, value);
                if(cachedValues.size() > 30){
                    String first = cachedValues.entrySet().iterator().next().getKey();
                    cachedValues.remove(first);
                }
            }
            return true;
        }
        return false;
    }

    public static long getLong(Context context, String key, long defValue, String preferenceName) {
        return getLong(context, key, defValue, preferenceName, true);
    }

    public static long getLong(Context context, String key, long defValue, String preferenceName, boolean fromCache) {
        if (fromCache) {
            LinkedHashMap<String, Long> cachedValues = cachedLongValues.get(preferenceName);
            if(cachedValues != null){
                Long cache = cachedValues.get(key);
                if(cache != null){
                    return cache;
                }
            }
        }

        SharedPreferences prefs = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        return prefs.getLong(key, defValue);
    }


    //====================== boolean ==========================

    public static boolean saveBoolean(Context context, String key, boolean value, String preferenceName) {
        return saveBoolean(context, key, value, preferenceName, true);
    }

    /**
     * 通用boolean的保存读取
     */
    public static boolean saveBoolean(Context context, String key, boolean value, String preferenceName, boolean cache) {
        SharedPreferences prefs = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (editor != null) {
            editor.putBoolean(key, value);
            editor.apply();
            if(cache){
                LinkedHashMap<String, Boolean> cachedValues = cachedBooleanValues.get(preferenceName);
                if(cachedValues == null){
                    cachedValues = new LinkedHashMap<>();
                    cachedBooleanValues.put(preferenceName, cachedValues);
                }

                cachedValues.put(key, value);
                if(cachedValues.size() > 30){
                    String first = cachedValues.entrySet().iterator().next().getKey();
                    cachedValues.remove(first);
                }
            }
            return true;
        }
        return false;
    }

    public static boolean getBoolean(Context context, String key, boolean defValue, String preferenceName) {
        return getBoolean(context, key, defValue, preferenceName, true);
    }

    public static boolean getBoolean(Context context, String key, boolean defValue, String preferenceName, boolean fromCache) {
        if (fromCache) {
            LinkedHashMap<String, Boolean> cachedValues = cachedBooleanValues.get(preferenceName);
            if(cachedValues != null){
                Boolean cache = cachedValues.get(key);
                if(cache != null){
                    return cache;
                }
            }
        }

        SharedPreferences prefs = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, defValue);
    }


    //====================== String ==========================
    /**
     * 通用String的保存读取
     */
    public static boolean saveString(Context context, String key, String value, String preferenceName) {
        SharedPreferences prefs = context.getSharedPreferences(preferenceName, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = prefs.edit();
        if (editor != null) {
            editor.putString(key, value);
            editor.apply();
            return true;
        }
        return false;
    }

    public static String getString(Context context, String key, String defValue, String preferenceName) {
        SharedPreferences prefs = context.getSharedPreferences(preferenceName, Context.MODE_MULTI_PROCESS);
        return prefs.getString(key, defValue);
    }


    //====================== float ==========================

    public static boolean saveFloat(Context context, String key, float value, String preferenceName) {
        return saveFloat(context, key, value, preferenceName, true);
    }

    /**
     * 通用float的保存读取
     */
    public static boolean saveFloat(Context context, String key, float value, String preferenceName, boolean cache) {
        SharedPreferences prefs = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (editor != null) {
            editor.putFloat(key, value);
            editor.apply();
            if(cache){
                LinkedHashMap<String, Float> cachedValues = cachedFloatValues.get(preferenceName);
                if(cachedValues == null){
                    cachedValues = new LinkedHashMap<>();
                    cachedFloatValues.put(preferenceName, cachedValues);
                }

                cachedValues.put(key, value);
                if(cachedValues.size() > 30){
                    String first = cachedValues.entrySet().iterator().next().getKey();
                    cachedValues.remove(first);
                }
            }
            return true;
        }
        return false;
    }

    public static float getFloat(Context context, String key, float defValue, String preferenceName) {
        return getFloat(context, key, defValue, preferenceName, true);
    }

    public static float getFloat(Context context, String key, float defValue, String preferenceName, boolean fromCache) {
        if (fromCache) {
            LinkedHashMap<String, Float> cachedValues = cachedFloatValues.get(preferenceName);
            if(cachedValues != null){
                Float cache = cachedValues.get(key);
                if(cache != null){
                    return cache;
                }
            }
        }

        SharedPreferences prefs = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        return prefs.getFloat(key, defValue);
    }



    //====================== all ==========================

    public static boolean remove(Context context, String key, String preferenceName) {
        SharedPreferences prefs = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (editor != null) {
            editor.remove(key);
            editor.apply();
            if (cachedIntValues.containsKey(key)) {
                cachedIntValues.remove(key);
            }
            if (cachedLongValues.containsKey(key)) {
                cachedLongValues.remove(key);
            }
            if (cachedBooleanValues.containsKey(key)) {
                cachedBooleanValues.remove(key);
            }
            if (cachedFloatValues.containsKey(key)) {
                cachedFloatValues.remove(key);
            }
            return true;
        }
        return false;
    }

    public static boolean haveKey(Context context, String key, String preferenceName) {
        SharedPreferences prefs = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        return prefs.contains(key);
    }

}