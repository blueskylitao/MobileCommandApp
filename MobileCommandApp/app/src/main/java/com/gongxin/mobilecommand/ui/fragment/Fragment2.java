package com.gongxin.mobilecommand.ui.fragment;

import android.graphics.Bitmap;
import android.os.Build;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.base.BaseFragment;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;

/**
 * author:geekLi
 */

public class Fragment2 extends BaseFragment {
    /* @BindView(R.id.login_btn_back)
     ImageView mLoginBtnBack;
     @BindView(R.id.tv_title)
     TextView mTvTitle;*/
    @BindView(R.id.webview_detail)
    WebView webview_detail;
    @BindView(R.id.pb_progress)
    ProgressBar pb_progress;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_page2, container, false);
        return rootView;
    }

    @Override
    protected void initListener() {
       /* mLoginBtnBack.setVisibility(View.GONE);
        mTvTitle.setText(R.string.main_tab2_txt);
*/
    }

    @Override
    protected void initData() {
        initWebView();
        webview_detail.loadUrl("http://47.104.161.130:8080/analystrunner/af0e0e15-d1c5-414a-bded-fb60c3c35f24");
    }

    private void initWebView() {
        webview_detail.setHorizontalScrollBarEnabled(false);
        webview_detail.setVerticalScrollBarEnabled(false);
        WebSettings settings = webview_detail.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);//解决图片不显示

        // 网页内容的宽度是否可大于WebView控件的宽度
        settings.setLoadWithOverviewMode(false);
        // 保存表单数据
        settings.setSaveFormData(true);
        settings.setDisplayZoomControls(false);
        // 启动应用缓存
        settings.setAppCacheEnabled(true);
        // 设置缓存模式
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 不缩放
        webview_detail.setInitialScale(100);
        // 排版适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // WebView是否新窗口打开(加了后可能打不开网页)
        // settings.setSupportMultipleWindows(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //支持缩放
        settings.setUseWideViewPort(true);//设定支持viewport
        settings.setLoadWithOverviewMode(true);//false 可能对屏幕适配有用
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);//设定支持缩放
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setDomStorageEnabled(true);//主要是这句 使用localStorage则必须打开
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);


        webview_detail.setWebChromeClient(new WebChromeClient() {

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
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pb_progress.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }
        });

       /* WebSettings webSettings = webview_detail.getSettings();
        webSettings.setDomStorageEnabled(true);//主要是这句
        webSettings.setJavaScriptEnabled(true);//启用js
        webSettings.setBlockNetworkImage(false);//解决图片不显示
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);


        webview_detail.setWebChromeClient(new WebChromeClient());//这行最好不要丢掉
        //该方法解决的问题是打开浏览器不调用系统浏览器，直接用webview打开
        webview_detail.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });*/

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
            ((ViewGroup) webview_detail.getParent()).removeView(webview_detail);
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
}
