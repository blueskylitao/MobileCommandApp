package com.gongxin.mobilecommand.global;


import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.multidex.MultiDexApplication;

import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.utils.HttpUtil;
import com.gongxin.mobilecommand.utils.SPUtil;
import com.gongxin.mobilecommand.utils.Utils;
import com.kook.common.INotificationView;
import com.kook.im.UIManager;
import com.kook.im.manager.IMPluginManager;
import com.kook.im.manager.action.Action;
import com.kook.im.manager.action.BasePluginItem;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;

public class MCApp extends MultiDexApplication {
    private static Context mCtx;

    @Override
    public void onCreate() {
        super.onCreate();
        mCtx = this;
        initOkHttp();
        //  initX5();
        initChatModel();

        String ipaddress = (String) SPUtil.get(getContext(), "ipaddress", "");
        if (!Utils.isNullOrEmpty(ipaddress)) {
            HttpUtil.BASEURL = ipaddress;
        }
        String ueipaddress = (String) SPUtil.get(getContext(), "ueipaddress", "");
        if (!Utils.isNullOrEmpty(ueipaddress)) {
            HttpUtil.UE_BASE_URL = ueipaddress;
        }
    }

    private void initChatModel() {

      /*  //初始化push
        if(AppTaskUtil.isCoreProcess(this)){
            String meizuAppId = "110486";
            String meizuAppKey = "c9199e848cd348669ef3cbc9467fc837";
            String xiaomiAppId = "2882303761517814516";
            String xiaomiAppKey = "5431781480516";
            PushUtil.registerMeizuPush(meizuAppId, meizuAppKey);
            PushUtil.registerXiaoMiPush(xiaomiAppId, xiaomiAppKey);
            PushUtil.init(this);
        }*/
        UIManager.init(getApplicationContext());
        //被强踢回调
        UIManager.registerFataError(s -> {
            Log.d("MyApp", "onFataErr msg = " + s);
            Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            return true;
        });
        IMPluginManager.getIns().addMessageMenu("id01", "弹一弹", BasePluginItem.SCOPE_GROUP | BasePluginItem.SCOPE_SINGLE
                , new Action() {
                    @Override
                    public void onAction(INotificationView iNotificationView, Map<String, String> map) {
                        String messageText = map.get("message_text");
                        Toast.makeText(iNotificationView.getContext(), messageText, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.themecolor, R.color.white);//全局设置主题颜色
                return new MaterialHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    private void initOkHttp() {
       /* HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);*/
        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
       /* HttpsUtils.SSLParams sslParams = null;
        try {
            sslParams = HttpsUtils.getSslSocketFactory(getAssets().open("custom.cer"));
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
        params.put("commonParamsKey2", "这里支持中文参数");
//-------------------------------------------------------------------------------------//
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        OkGo.getInstance().init(this)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers)                      //全局公共头
                .addCommonParams(params);                       //全局公共参数

    }


    public static Context getContext() {
        return mCtx;
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
