package com.gongxin.mobilecommand.ui.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.base.PadBaseFragment;
import com.gongxin.mobilecommand.domain.UrlMessageEvent;
import com.gongxin.mobilecommand.ui.activity.PublicWebviewActivity;
import com.gongxin.mobilecommand.utils.HttpUtil;
import com.gongxin.mobilecommand.utils.SPUtil;
import com.gongxin.mobilecommand.utils.ToastUtil;
import com.gongxin.mobilecommand.utils.Utils;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * author:geekLi
 */

public class BrowserFragment extends PadBaseFragment {

    private final String TAG = BrowserFragment.class.getSimpleName();
    @BindView(R.id.webview_detail)
    WebView webview_detail;
    @BindView(R.id.pb_progress)
    ProgressBar pb_progress;
    @BindView(R.id.ll_line_web)
    LinearLayout mLlline;
    @BindView(R.id.ib_update)
    ImageButton mIbUpdate;
    String dataUrl = "";
    String isJump = "0";
    String token;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_page2, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            dataUrl = bundle.getString("url");
            isJump = bundle.getString("isjump");
        }
        return rootView;
    }

    @Override
    protected void initListener() {
        EventBus.getDefault().register(this);
        mIbUpdate.setOnClickListener(view -> webview_detail.reload());
    }

    @Override
    protected void initData() {
        initWebView();
        token = (String) SPUtil.get(context, "usertoken", "0");
        if (!TextUtils.isEmpty(dataUrl)) {
            webview_detail.loadUrl(dataUrl);
            // webview_detail.loadUrl("http://www.oschina.net/question/54100_34836");
        } else {
            ToastUtil.showToast(context, "空的URL");
        }
    }

    private void initWebView() {
        webview_detail.setHorizontalScrollBarEnabled(false);
        webview_detail.setVerticalScrollBarEnabled(false);
        //webview_detail.setScrollBarStyle(0);
        webview_detail.setLayerType(View.LAYER_TYPE_HARDWARE, null);//开启硬件加速

        WebSettings settings = webview_detail.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);//解决图片不显示

        // 网页内容的宽度是否可大于WebView控件的宽度
        // settings.setLoadWithOverviewMode(true);
        // 保存表单数据
        settings.setSaveFormData(true);
        // 启动应用缓存
        settings.setAppCacheEnabled(true);
        // 设置缓存模式
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 不缩放
        webview_detail.setInitialScale(100);
        // 排版适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //支持缩放
        settings.setUseWideViewPort(true);//设定支持viewport
        settings.setLoadWithOverviewMode(true);//false 可能对屏幕适配有用
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);//设定支持缩放
        settings.setDomStorageEnabled(true);//主要是这句 使用localStorage则必须打开
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setDisplayZoomControls(false); //隐藏原生的缩放控件`
        //  webview_detail.setScaleX(40);

       /* DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        Log.d("maomao", "densityDpi = " + mDensity);
        if (mDensity == 240) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if(mDensity == 120) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else if (mDensity == DisplayMetrics.DENSITY_TV){
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else{
            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);*/


        webview_detail.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int position) {
                pb_progress.setProgress(position);
                if (position == 100) {
                    pb_progress.setVisibility(View.GONE);
                    if ("1".equals(isJump)) {
                        mLlline.setVisibility(View.VISIBLE);
                    } else {
                        mLlline.setVisibility(View.GONE);
                    }
                }
                super.onProgressChanged(view, position);
            }
        });

        webview_detail.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("shouldOverride", "shouldOverrideUrlLoading: " + url);

                if (url != null && url.contains("HomePopFlag=1")) {

                    Intent intent = new Intent(context, PublicWebviewActivity.class);
                    intent.putExtra("target_url", url + "&token=" + token);
                    intent.putExtra("showTitle", false);
                    startActivity(intent);
                    return true;
                } else {
                    webview_detail.loadUrl(url);
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                Log.d("shouldOverride", "shouldOverrideUrlLoading: " + url);

                if (url != null && url.contains("HomePopFlag=1")) {

                    Intent intent = new Intent(context, PublicWebviewActivity.class);
                    intent.putExtra("target_url", url + "&token=" + token);
                    intent.putExtra("showTitle", false);
                    startActivity(intent);
                    return true;
                } else {
                    webview_detail.loadUrl(url);
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("onPageStarted", "shouldOverrideUrlLoading: " + url);

                pb_progress.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }
        });

        webview_detail.setOnKeyListener((view, keyCode, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //这里处理返回键事件
                    if (webview_detail.canGoBack()) {
                        webview_detail.goBack();
                        return true;
                    }
                }
            }
            return false;
        });
    }

    @Override
    public void onDestroy() {
        if (webview_detail != null) {
            webview_detail.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webview_detail.clearHistory();
            webview_detail.clearCache(true);
            ((ViewGroup) webview_detail.getParent()).removeView(webview_detail);
            webview_detail.setWebChromeClient(null);
            webview_detail.setWebViewClient(null);
            webview_detail.destroy();
            webview_detail = null;

        }
        super.onDestroy();
    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(UrlMessageEvent messageEvent) {

        String url = messageEvent.getUrl();
        if (!Utils.isNullOrEmpty(url) && "1".equals(isJump)) {
            String ueUrl = HttpUtil.checkUeUrl(url);
            Log.e(TAG, "Event: " + ueUrl);
            webview_detail.loadUrl(ueUrl);
        }
    }
}
