package com.gongxin.mobilecommand.utils;

import android.text.TextUtils;

public class HttpUtil {
    public enum RequestMethod {
        GET, POST
    }

    //  public static String BASEURL = "http://172.30.11.92:8080/";
    //public static String BASEURL = "http://39.98.40.7:8080/";sp

    //public static String BASEURL = "http://47.104.161.130:8060/";
    public static String BASEURL = "http://47.104.161.130:8060/";
    public static String UE_BASE_URL = "http://47.104.161.130:8080/";
    //public static String BASEURL = "http://39.98.40.7:8072/";

    public static String checkUeUrl(String url){
        if (!TextUtils.isEmpty(url) && !url.startsWith("http://") && !url.startsWith("https://")) {
            return UE_BASE_URL+url;
        }
        return url;
    }
}