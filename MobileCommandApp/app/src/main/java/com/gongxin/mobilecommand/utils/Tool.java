package com.gongxin.mobilecommand.utils;

import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.net.Uri;
import android.text.ClipboardManager;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tool {
    /**
     * 分享文字信息给某个好友或者好友群 微信无法同时分享文本和图片
     *
     * @param context
     * @param info    要发送的文本
     */
    public static void sendTxt2WX(Context context, String info) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.SEND");
        intent.setType("image/*,text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, info);
        context.startActivity(intent);
    }

    /**
     * 分享文件给某个好友或者好友群 微信无法同时分享文本和图片
     *
     * @param context
     * @param path    要发送的文件路径
     * @param title   分享提示
     */
    public static void sendFile2WX(Context context, String path, String title) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.SEND");
        intent.setType("image/*,text/plain");
        // 加下面这句话有提示，不加则直接提示转发
        intent.putExtra(Intent.EXTRA_TEXT, title);
        File file = new File(path);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        context.startActivity(intent);
    }

    /**
     * 微信朋友圈只能分享图片
     *
     * @param context
     */
    public static void sendTimeLine(Context context, String path) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.SEND");
        intent.setType("image/*");
        File file = new File(path);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        context.startActivity(intent);
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static double getDistance(double lat1, double lon1, double lat2,
                                     double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);

        DecimalFormat df = new DecimalFormat("0.0");
        double d = results[0];
        String db = df.format(d);
        return Double.parseDouble(db);
    }

    public static void showPhoneClickDailog(final Context context,
                                            final String phone) {
        Builder builder = new Builder(context);
        builder.setItems(new String[]{"复制到剪贴板", "拨打电话", "发送短信"},
                new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                ClipboardManager cmb = (ClipboardManager) context
                                        .getSystemService(Context.CLIPBOARD_SERVICE);
                                cmb.setText(phone);
                                break;
                            case 1:
                                // 用intent启动拨打电话
                                // Intent intent = new
                                // Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));

                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri
                                        .parse("tel:" + phone));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                break;
                            case 2:
                                Uri smsToUri = Uri.parse("smsto:" + phone);
                                Intent intent2 = new Intent(Intent.ACTION_SENDTO,
                                        smsToUri);
                                intent2.putExtra("sms_body", "");
                                context.startActivity(intent2);
                                break;
                            default:
                                break;
                        }

                    }
                });
        builder.create().show();
    }

    public static int getVersionCode(Context context)// 获取版本号(内部识别号)
    {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return pi.versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }

    public static String getVersion(Context context)// 获取版本号
    {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return pi.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }
    //获取手机IMEI码
	/*public static String getImei(Context context){
		TelephonyManager telephonyManager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei=telephonyManager.getDeviceId();
		return imei;
	}*/

    /**
     * 调用选择浏览器界面打开某个地址
     *
     * @param context
     * @param url
     */
    public static void startBrowser(Context context, String url) {
        Uri uri = Uri.parse(url.toString());
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(it);
    }

    /**
     * 显示dialog
     */
    public static void showDialog(Context context, String title,
                                  final String[] items, final OnSelectListener listener) {

        Builder builder = new Builder(context);
        builder.setItems(items, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO
                listener.onselect(items[which]);
            }
        });
        builder.create().show();
    }

    public interface OnSelectListener {
        void onselect(String name);
    }


}
