/*
package com.gongxin.mobilecommand.ui.fragment;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.base.BaseFragment;
import com.gyf.immersionbar.ImmersionBar;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;

*/
/**
 * author:geekLi
 *//*


public class Fragment3Old extends BaseFragment {
    @BindView(R.id.login_btn_back)
    ImageView mLoginBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.webview_detail)
    com.tencent.smtt.sdk.WebView webview_detail;
    @BindView(R.id.pb_progress)
    ProgressBar pb_progress;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_data, container, false);
        return rootView;
    }

    @Override
    protected void initListener() {
        mLoginBtnBack.setVisibility(View.GONE);
        mTvTitle.setText(R.string.main_tab3_txt);

    }

    @Override
    protected void initData() {
        // webview_detail.setWebViewClient(new WebViewClient());
        initWebView();
        webview_detail.loadUrl("http://47.104.161.130:8080/analystrunner/af0e0e15-d1c5-414a-bded-fb60c3c35f24");
      */
/*   webview_detail.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int position) {
                pb_progress.setProgress(position);
                if (position == 100) {
                    pb_progress.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, position);
            }
        });

        webview_detail.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pb_progress.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }
        });*//*

    }

    private void initWebView() {
       */
/* WebSettings settings = webview_detail.getSettings();
        settings.setJavaScriptEnabled(true);

        //支持缩放
        settings.setUseWideViewPort(true);//设定支持viewport
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);//设定支持缩放

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
        });*//*

        com.tencent.smtt.sdk.WebSettings ws = webview_detail.getSettings();
        // 网页内容的宽度是否可大于WebView控件的宽度
        ws.setLoadWithOverviewMode(false);
        // 保存表单数据
        ws.setSaveFormData(true);
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);
        // 启动应用缓存
        ws.setAppCacheEnabled(true);
        // 设置缓存模式
        ws.setCacheMode(com.tencent.smtt.sdk.WebSettings.LOAD_DEFAULT);
        // setDefaultZoom  api19被弃用
        // 设置此属性，可任意比例缩放。
        ws.setUseWideViewPort(true);
        // 不缩放
        webview_detail.setInitialScale(100);
        // 告诉WebView启用JavaScript执行。默认的是false。
        ws.setJavaScriptEnabled(true);
        //  页面加载好以后，再放开图片
        ws.setBlockNetworkImage(false);
        // 使用localStorage则必须打开
        ws.setDomStorageEnabled(true);
        // 排版适应屏幕
        ws.setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // WebView是否新窗口打开(加了后可能打不开网页)
        ws.setSupportMultipleWindows(true);

        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。MIXED_CONTENT_ALWAYS_ALLOW
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(com.tencent.smtt.sdk.WebSettings.LOAD_NORMAL);
        }
        */
/** 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)*//*

        ws.setTextZoom(100);

        webview_detail.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        webview_detail.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsConfirm(WebView arg0, String arg1, String arg2,
                                       JsResult arg3) {
                return super.onJsConfirm(arg0, arg1, arg2, arg3);
            }

            @Override
            public boolean onJsAlert(WebView arg0, String arg1, String arg2,
                                     JsResult arg3) {
                */
/**
                 * 这里写入你自定义的window alert
                 *//*

                return super.onJsAlert(null, arg1, arg2, arg3);
            }
        });
        webview_detail.addJavascriptInterface(new MyJavascriptInterface(getActivity()), "injectedObject");
    }


    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.themecolor)
                .init();
    }

    @Override
    public void onDestroy() {
        try {
            if (webview_detail != null) {
                webview_detail.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
                webview_detail.stopLoading();
                webview_detail.setWebChromeClient(null);
                webview_detail.setWebViewClient(null);
                webview_detail.destroy();
                webview_detail = null;
            }
        } catch (Exception e) {
            Log.e("X5WebViewActivity", e.getMessage());
        }
        super.onDestroy();
    }
}
*/
