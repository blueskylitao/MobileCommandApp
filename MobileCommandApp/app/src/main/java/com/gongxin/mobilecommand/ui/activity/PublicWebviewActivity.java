package com.gongxin.mobilecommand.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.base.BaseActivity;
import com.gongxin.mobilecommand.domain.MessageEvent;
import com.gongxin.mobilecommand.utils.CRequest;
import com.gongxin.mobilecommand.utils.ForwardCommand;
import com.gongxin.mobilecommand.utils.SPUtil;
import com.gongxin.mobilecommand.utils.ToastUtil;
import com.gongxin.mobilecommand.utils.Utils;
import com.kook.im.model.forword.ForwardItem;
import com.kook.im.model.forword.ForwardUtils;
import com.kook.im.ui.choose.ChooseActivity;
import com.kook.im.util.choose.ChooseType;
import com.kook.im.util.choose.datasource.ChooseDataSrcProvider;
import com.kook.im.util.choose.entity.ChooseResultNode;
import com.kook.sdk.api.EConvType;
import com.kook.sdk.wrapper.msg.model.KKIMMessageFactory;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;

public class PublicWebviewActivity extends BaseActivity {

    @BindView(R.id.webview_detail)
    WebView mWebView;
    @BindView(R.id.pb_progress)
    ProgressBar mPbProgress;
    @BindView(R.id.public_webview_top)
    LinearLayout public_webview_top;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.login_btn_back)
    ImageView mBack;
    private boolean showTitle;
    private String url = "", token;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.fragment_page2);
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void setListener() {
        Intent intent = getIntent();
        url = intent.getStringExtra("target_url");
        String title = intent.getStringExtra("title");
        showTitle = intent.getBooleanExtra("showTitle", true);
        if (showTitle) {
            public_webview_top.setVisibility(View.VISIBLE);
            mTvTitle.setText(title);
        } else {
            public_webview_top.setVisibility(View.GONE);
        }

        mBack.setOnClickListener(view -> finish());
    }

    @Override
    protected void processLogic() {
        token = (String) SPUtil.get(context, "usertoken", "0");
        initWebView();
        mWebView.loadUrl(url);
    }

    private void initWebView() {
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setScrollBarStyle(0);
        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);//开启硬件加速
        WebSettings settings = mWebView.getSettings();
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
        settings.setCacheMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        // 不缩放
        mWebView.setInitialScale(100);
        // 排版适应屏幕
        // settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // WebView是否新窗口打开(加了后可能打不开网页)
        //settings.setSupportMultipleWindows(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //支持缩放
        settings.setUseWideViewPort(true);//设定支持viewport
        settings.setLoadWithOverviewMode(false);//false 可能对屏幕适配有用
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);//设定支持缩放
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setDomStorageEnabled(true);//主要是这句 使用localStorage则必须打开
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);


        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int position) {
                mPbProgress.setProgress(position);
                if (position == 100) {
                    mPbProgress.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, position);
            }
        });

        mWebView.setWebViewClient(new MyWebViewClient());
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url != null && url.contains("backType=finishAcivity")) {
                finish();
                return true;
            } else if (url != null && url.contains("iconUrl")) {

                Map<String, String> mapRequest = CRequest.URLRequest(url);
                String linkUrl = mapRequest.get("linkurl");
                String typeID = mapRequest.get("typeid");
                String iconUrl = mapRequest.get("iconurl");
                String type = mapRequest.get("type");
                String content = mapRequest.get("content");
                String title = mapRequest.get("title");
                String trasLinkUrl = "";
                String trasIconUrl = "";
                String trasContent = "";
                String trasTitle = "";

                try {
                    if (!Utils.isNullOrEmpty(linkUrl)) {
                        trasLinkUrl = URLDecoder.decode(linkUrl, "UTF-8");
                    }
                    if (!Utils.isNullOrEmpty(iconUrl)) {
                        trasIconUrl = URLDecoder.decode(iconUrl, "UTF-8");
                    }
                    if (!Utils.isNullOrEmpty(content)) {
                        trasContent = URLDecoder.decode(content, "UTF-8");
                    }
                    if (!Utils.isNullOrEmpty(title)) {
                        trasTitle = URLDecoder.decode(title, "UTF-8");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //  ChatActivity.launch(context, Long.parseLong("1900024812292569"), EConvType.ECONV_TYPE_GROUP, "");
                ForwardItem forwardItem = ForwardUtils.buidForward(KKIMMessageFactory.createLinkCardMsg(EConvType.ECONV_TYPE_SINGLE, 1, trasLinkUrl, trasTitle, trasContent, trasIconUrl, true));
                ForwardCommand forwardCommand = new ForwardCommand() {
                    @Override
                    public void onChooseResult(SoftReference<com.kook.im.ui.BaseActivity> context, ArrayList<ChooseResultNode> arrayList) {
                        super.onChooseResult(context, arrayList);
                        for (ChooseResultNode baseMultiItemEntity : arrayList) {
                            if (baseMultiItemEntity.getDataType() == ChooseType.USER_TYPE) {
                                //用户id
                                ToastUtil.showToast(PublicWebviewActivity.this, "用户id" + baseMultiItemEntity.getId());
                            }
                            if (baseMultiItemEntity.getDataType() == ChooseType.GROUP_TYPE) {
                                //群id
                                ToastUtil.showToast(PublicWebviewActivity.this, "群id" + baseMultiItemEntity.getId());
                            }
                        }
                    }
                };
                forwardCommand.setForwardItem(forwardItem);
                ChooseActivity.launch(context, forwardCommand, ChooseDataSrcProvider.getForwardOption(context.getString(R.string.forword_choose_out_msg)));
                return true;
            } else {
                String tokenUrl = url;
                mWebView.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            if (url != null && url.contains("backType=finishAcivity")) {
                finish();
                return true;
            } else if (url != null && url.contains("iconUrl")) {

                Map<String, String> mapRequest = CRequest.URLRequest(url);
                String linkUrl = mapRequest.get("linkurl");
                String typeID = mapRequest.get("typeid");
                String iconUrl = mapRequest.get("iconurl");
                String type = mapRequest.get("type");
                String content = mapRequest.get("content");
                String title = mapRequest.get("title");
                String trasLinkUrl = "";
                String trasIconUrl = "";
                String trasContent = "";
                String trasTitle = "";

                try {
                    if (!Utils.isNullOrEmpty(linkUrl)) {
                        trasLinkUrl = URLDecoder.decode(linkUrl, "UTF-8");
                    }
                    if (!Utils.isNullOrEmpty(iconUrl)) {
                        trasIconUrl = URLDecoder.decode(iconUrl, "UTF-8");
                    }
                    if (!Utils.isNullOrEmpty(content)) {
                        trasContent = URLDecoder.decode(content, "UTF-8");
                    }
                    if (!Utils.isNullOrEmpty(title)) {
                        trasTitle = URLDecoder.decode(title, "UTF-8");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("transContent",trasContent);
                Log.e("trasLinkUrl",trasLinkUrl);
                Log.e("trasTitle",trasTitle);
                Log.e("trasIconUrl",trasIconUrl);
                //  ChatActivity.launch(context, Long.parseLong("1900024812292569"), EConvType.ECONV_TYPE_GROUP, "");
                ForwardItem forwardItem = ForwardUtils.buidForward(KKIMMessageFactory.createLinkCardMsg(EConvType.ECONV_TYPE_SINGLE, 1, trasLinkUrl, trasTitle, trasContent, trasIconUrl, true));
                ForwardCommand forwardCommand = new ForwardCommand() {
                    @Override
                    public void onChooseResult(SoftReference<com.kook.im.ui.BaseActivity> context, ArrayList<ChooseResultNode> arrayList) {
                        super.onChooseResult(context, arrayList);
                        for (ChooseResultNode baseMultiItemEntity : arrayList) {
                            if (baseMultiItemEntity.getDataType() == ChooseType.USER_TYPE) {
                                //用户id
                                ToastUtil.showToast(PublicWebviewActivity.this, "用户id" + baseMultiItemEntity.getId());
                            }
                            if (baseMultiItemEntity.getDataType() == ChooseType.GROUP_TYPE) {
                                //群id
                                ToastUtil.showToast(PublicWebviewActivity.this, "群id" + baseMultiItemEntity.getId());
                            }
                        }
                    }
                };
                forwardCommand.setForwardItem(forwardItem);
                ChooseActivity.launch(context, forwardCommand, ChooseDataSrcProvider.getForwardOption(context.getString(R.string.forword_choose_out_msg)));
                return true;
            } else {
                mWebView.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (url != null && url.contains("backType=finishAcivity")) {
                finish();
            } else {
                super.onPageFinished(view, url);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       /* if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.getUrl() != null && mWebView.getUrl().contains("backType=finishAcivity")) {
            finish();
        } else */
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            mWebView.clearCache(true);
            mWebView.clearHistory();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            mWebView.clearCache(true);
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.destroy();
            mWebView = null;
            // setGoBack();
        }
        super.onDestroy();
    }

    private void setGoBack() {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setBack(true);
        EventBus.getDefault().post(messageEvent);
    }
}
