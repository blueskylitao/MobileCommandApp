package com.gongxin.mobilecommand.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.gongxin.mobilecommand.global.MCApp;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static final String TAG = "PushDemoActivity";
    public static final String RESPONSE_METHOD = "method";
    public static final String RESPONSE_CONTENT = "content";
    public static final String RESPONSE_ERRCODE = "errcode";
    protected static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
    public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
    public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
    public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
    protected static final String EXTRA_ACCESS_TOKEN = "access_token";
    public static final String EXTRA_MESSAGE = "message";

    public static String logStringCache = "";
    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;


    // 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    // 用share preference来实现是否绑定的开关。在ionBind且成功时设置true，unBind且成功时设置false
    public static boolean hasBind(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String flag = sp.getString("bind_flag", "");
        return "ok".equalsIgnoreCase(flag);
    }

    public static void setBind(Context context, boolean flag) {
        String flagStr = "not";
        if (flag) {
            flagStr = "ok";
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("bind_flag", flagStr);
        editor.commit();
    }

    public static List<String> getTagsList(String originalText) {
        if (originalText == null || originalText.equals("")) {
            return null;
        }
        List<String> tags = new ArrayList<String>();
        int indexOfComma = originalText.indexOf(',');
        String tag;
        while (indexOfComma != -1) {
            tag = originalText.substring(0, indexOfComma);
            tags.add(tag);

            originalText = originalText.substring(indexOfComma + 1);
            indexOfComma = originalText.indexOf(',');
        }

        tags.add(originalText);
        return tags;
    }

    public static void showSoftInput(Context context, EditText edit) {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(edit, 0);
    }

    public static String getLogText(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString("log_text", "");
    }

    public static void setLogText(Context context, String text) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("log_text", text);
        editor.commit();
    }

    public static String getAppVersion(Context context) {
        String version = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo info;
            info = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            return version;
        }
        return version;

    }

    public static String getPhoneModel() {
        try {
            return android.os.Build.MODEL;
        } catch (Exception e) {
            return "";
        }

    }

    public static String getOS() {
        try {
            return "Android " + android.os.Build.VERSION.RELEASE;
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * 隐藏软键盘
     *
     * @param activity 要隐藏软键盘的activity
     */
    public static void hideSoftKeyBoard(Activity activity) {
        final View v = activity.getWindow().peekDecorView();
        if (v != null && v.getWindowToken() != null) {
            try {
                ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isMunicipality(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        return str.contains("北京") || str.contains("上海") || str.contains("重庆") || str.contains("天津");
    }

    public static boolean isNullOrEmpty(String str) {
        return !(null != str && !TextUtils.isEmpty(str.trim()));
    }

    // 是否有SD卡
    public static boolean checkSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static void json2Url(String url, org.json.JSONObject jsonObject) {
        try {
            Iterator<String> i = jsonObject.keys();
            String params = "";
            while (i.hasNext()) {
                String ee = i.next();
                String value = jsonObject.getString("" + ee);
                params += ee + "=" + value + "&";
            }
            Logger.e("URL", url + "?" + params.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Logger.e("URL", "网络请求失败");
        }
    }

    public static Intent createAlbumIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        Intent chooseIntent = Intent.createChooser(intent, null);
        return chooseIntent;
    }

    /**
     * 拍照
     *
     * @return
     */
    public static Intent createShotIntent(File tempFile) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        Uri uri = Uri.fromFile(tempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    public static Intent createShotIntent2(File tempFile) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        Uri uri = Uri.fromFile(tempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    public static boolean isNetWorkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    //是否连接WIFI
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiNetworkInfo.isConnected();

    }

    public static String getFormatgTime(long time) {
        DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd");
        String timeStr = df3.format(time);
        return timeStr;
    }

    public static String getFormatgTime2(long time) {
        DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = df3.format(time);
        return timeStr;
    }

    public static String getFormatgTime3(long time) {
        DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timeStr = df3.format(time);
        return timeStr;
    }

    private static final String gmtFormat = "EEE, dd MMM yyyy HH:mm:ss z";

    public static Date convertGMTStringDate(String time) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat(gmtFormat, Locale.US);

        inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return inputFormat.parse(time);
    }

    //防止按钮多次点击
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 3000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    //防止语音验证码按钮多次点击
    private static long lastClickTime2;
    public static int restTime;

    public static boolean isFastDoubleClick2() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime2;
        if (0 < timeD && timeD < 60000) {
            restTime = Math.round((60 * 1000 - timeD) / 1000);
            return true;
        }
        lastClickTime2 = time;
        return false;
    }

    public static void createShortCut(Activity act, int iconResId,
                                      int appnameResId) {

        // com.android.launcher.permission.INSTALL_SHORTCUT


        Intent shortcutintent = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重复创建
        shortcutintent.putExtra("duplicate", false);
        // 需要现实的名称
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                act.getString(appnameResId));
        // 快捷图片
        Parcelable icon = Intent.ShortcutIconResource.fromContext(
                act.getApplicationContext(), iconResId);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        // 点击快捷图片，运行的程序主入口
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                new Intent(act.getApplicationContext(), act.getClass()));
        // 发送广播
        act.sendBroadcast(shortcutintent);
    }
    //获取手机ip地址

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

    public static String getFormatTime(long time) {
        String formatTime = null;
        Date dt = new Date();
        long l = dt.getTime() - time;
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        if (min == 0) {
            formatTime = "刚刚";
        }
        if (min > 0) {
            formatTime = "" + min + "分前";
        }
        if (hour > 0) {
            formatTime = "" + hour + "小时前";
        }
        if (day > 0) {
            formatTime = "" + day + "天前";
        }
        if (day > 30) {
            formatTime = "一个月前";
        }
        if (day > 90) {
            formatTime = "三个月前";
        }
        if (day > 100) {
            formatTime = "很久之前";
        }
        return formatTime;
    }

    /**
     * 显示时间格式为今天、昨天、yyyy/MM/dd hh:mm
     *
     * @param context
     * @param seconds
     * @return String
     */
    public static String formatTimeString(Context context, long seconds) {
        long timeInmillSeconds = seconds * 1000;
        Time then = new Time();
        then.set(timeInmillSeconds);
        Time now = new Time();
        now.setToNow();

        String formatStr;
        if (then.year != now.year) {
            formatStr = "yyyy/MM/dd";
        } else if (then.yearDay != now.yearDay && ((now.yearDay - then.yearDay) == 1)) {
            // If it is from a different day than today, show only the date.
            formatStr = "HH:mm";
        } else if (then.yearDay != now.yearDay && ((now.yearDay - then.yearDay) > 1)) {
            formatStr = "MM/dd";
        } else {
            // Otherwise, if the message is from today, show the time.
            formatStr = "HH:mm";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        String temp = sdf.format(new Date(timeInmillSeconds));
        if (then.year == now.year && then.yearDay == now.yearDay) {
            return temp;
        } else if ((then.year == now.year) && ((now.yearDay - then.yearDay) == 1)) {
            return "昨天";
        } else if ((then.year == now.year) && ((now.yearDay - then.yearDay) > 1)) {
            return temp;
        } else {

			/*if (temp != null && temp.length() == 5 && temp.substring(0, 1).equals("0")) {
                temp = temp.substring(1);
			}*/
            return temp;
        }
    }

    public static boolean isCloseEnough(long latertime, long earlytime) {
        long count = (latertime - earlytime);
        return count < 60;
    }

    /**
     * pswFilter: if the param folow the password match rule
     */
    public static boolean pswFilter(CharSequence s) {
        if (TextUtils.isEmpty(s)) {
            return false;
        }
        String PASSWORD_REGEX = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,}$";
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    public static boolean isAppOnForeground(Context context) {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    public static String storageFolder = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "liveness" + File.separator;

    public static int getImageCountInPath(String path) {
        int i = 0;
        File file = new File(path);
        File[] files = file.listFiles();
        for (int j = 0; j < files.length; j++) {
            String name = files[j].getName();
            if (files[j].isDirectory()) {
                String dirPath = files[j].toString().toLowerCase();
                getImageCountInPath(dirPath + "/");
            } else if (files[j].isFile() & name.endsWith(".jpg")) {
                i++;
            }
        }
        return i;
    }

    public static List<String> getImageListName(String path) {
        List<String> imageList = new ArrayList<>();
        File file = new File(path);
        File[] files = file.listFiles();
        for (int j = 0; j < files.length; j++) {
            if (files[j].isFile() & files[j].getName().endsWith(".jpg")) {
                imageList.add(files[j].getName());
            }
        }
        return imageList;
    }

    /**
     * 加载本地图片
     *
     * @param url the file url
     * @return Bitmap
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static float dpToPixel(float dp) {
        return dp * (getDisplayMetrics().densityDpi / 160F);
    }

    public static DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) MCApp.getContext().getSystemService(
                Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(
                displaymetrics);
        return displaymetrics;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top == 0 ? 60 : rect.top;
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        //activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        InputMethodManager imm = (InputMethodManager) MCApp.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }


    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * Return the context of Application object.
     *
     * @return the context of Application object
     */
    public static Application getApp() {
        if (sApplication != null) return sApplication;
        throw new NullPointerException("u should init first");
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static String getIpAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            // 3/4g网络
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                //  wifi网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());
                return ipAddress;
            } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                // 有限网络
                return getLocalIp();
            }
        }
        return null;
    }

    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    // 获取有限网IP
    private static String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return "0.0.0.0";

    }
}
