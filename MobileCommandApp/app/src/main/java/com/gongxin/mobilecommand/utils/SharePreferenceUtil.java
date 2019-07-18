package com.gongxin.mobilecommand.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gongxin.mobilecommand.domain.LoginUser;

import java.util.ArrayList;
import java.util.List;

public class SharePreferenceUtil {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private static SharePreferenceUtil util;

    public SharePreferenceUtil(Context context, String file) {
        if (file == null || context == null)
            return;
        sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static SharePreferenceUtil getUtil(Context context) {
        if (util == null) {
            util = new SharePreferenceUtil(context, "jcc");
        }
        return util;
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }

    //
    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    //
    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    //
    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    //
    public void putLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public long getLong(String key, long defValue) {
        return sp.getLong(key, defValue);
    }

    //
    public void putFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public Float getFloat(String key, Float defValue) {
        return sp.getFloat(key, defValue);
    }

    /**
     * 保存List
     *
     * @param tag
     * @param datalist
     */
    public <T> void setDataList(String tag, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;

        //转换成json数据，再保存
        String strJson = JSON.toJSONString(datalist);
        //editor.clear();
        editor.putString(tag, strJson);
        editor.commit();
    }

    /**
     * 清空List
     *
     * @param tag
     * @param datalist
     */
    public <T> void clearDataList(String tag, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0) {
            //  editor.clear();
            editor.putString(tag, "");
            editor.commit();
        } else {
            return;
        }
    }

    /**
     * 获取List
     *
     * @param tag
     * @return
     */
    public <T> List<T> getDataList(String tag) {
        List<T> datalist = new ArrayList<T>();
        String strJson = sp.getString(tag, null);
        if (null == strJson || TextUtils.isEmpty(strJson)) {
            return datalist;
        }
        JSONArray objects = JSON.parseArray(strJson);
        for (int i = 0; i < objects.size(); i++) {
            datalist.add((T) objects.get(i));
        }
        return datalist;

    }

    /**
     * 获取个人信息JSON
     *
     * @param tag
     * @return
     */
    public List<LoginUser> getJsonDataList(String tag) {
        List<LoginUser> datalist = new ArrayList<LoginUser>();
        String strJson = sp.getString(tag, null);
        if (null == strJson || TextUtils.isEmpty(strJson)) {
            return datalist;
        }
        datalist = JSON.parseArray(strJson, LoginUser.class);

        return datalist;
    }

}
