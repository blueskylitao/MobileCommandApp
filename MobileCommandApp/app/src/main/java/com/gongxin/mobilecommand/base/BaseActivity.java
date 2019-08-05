
package com.gongxin.mobilecommand.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.global.Constants;
import com.gongxin.mobilecommand.ui.activity.login.LoginActivity;
import com.gongxin.mobilecommand.ui.dialog.SelectDialog;
import com.gongxin.mobilecommand.utils.AppManager;
import com.gongxin.mobilecommand.utils.HttpUtil;
import com.gongxin.mobilecommand.utils.SPUtil;
import com.gongxin.mobilecommand.utils.ToastUtil;
import com.gyf.immersionbar.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {
    protected Context context = this;
    protected Activity activity = this;
    protected ProgressDialog pDialog;
    private String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBeforeSetContentLayout();
        //初始化沉浸式
        ImmersionBar.with(this)
                .fitsSystemWindows(false)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                //   .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .init();
        //禁止横屏
      //  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
        ButterKnife.bind(this);

        initdata();
        AppManager.getAppManager().addActivity(this);
        Logger.d("当前Activity 栈中有：" + AppManager.getAppManager().getActivityCount() + "个Activity");
    }

    private void initView() {
        loadViewLayout();
    }

    private void initdata() {
        findViewById();
        setListener();
        processLogic();
    }

    public void httpRequestByPost(String url, Map<String, String> requestData,
                                  final int requestId) throws Exception {
        requestData.put("appId", Constants.APP_SECRET);
        requestData.put("timestamp", System.currentTimeMillis() + "");
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(requestData));
        HttpHeaders header = new HttpHeaders();
        header.put("token", SPUtil.get(context, "usertoken", "null") + "");
        httpRequestByPostFinal(url, header, jsonObject, requestId);
    }

    public void httpRequestByGet(String url, HttpParams params,
                                 final int requestId) throws Exception {
        HttpHeaders header = new HttpHeaders();
        //header.put("token", SPUtil.get(context, "usertoken", "null") + "");
        httpRequestByGetFinal(url, header, params, requestId);
    }
    /*  public void httpRequestByPost(String url, JSONObject requestData, int pageNum,
                                    final int requestId) throws Exception {
          JSONObject json = new JSONObject();
          json.put("appId", Constants.APP_SECRET);
          json.put("reqData", requestData.toJSONString());
          json.put("token", SPUtil.get(context, "usertoken", "null"));
          json.put("timestamp", System.currentTimeMillis());
          json.put("pageNum", pageNum);

          httpRequestByPost(url, json, requestId);
      }
  */

    public void requestGetSmsCode(String phone, String source, int requestId) {

        showProgressDialog(getString(R.string.dialog_loading));
        try {
            Map<String, String> params = new HashMap<>();
            params.put("source", source);
            params.put("phoneNo", phone);
            httpRequestByPost("app/home/consumer/sendMessage", params, requestId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void httpRequestByPostFinal(String url, HttpHeaders header, JSONObject params, final int requestId) {


        String finalUrl = HttpUtil.BASEURL + url;
        Log.e(TAG, finalUrl );
        OkGo.<String>post(finalUrl)
                .tag(this)
                .headers(header)
                .upJson(JSON.toJSONString(params))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dismissDialog();
                        onHttpRequestResult(response, requestId);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        dismissDialog();
                        onHttpRequestErr(response, requestId);
                    }
                });
    }

    public void httpRequestUploadByPost(String url, HashMap<String, String> params, List<File> list, final int requestId) {


        OkGo.<String>post(HttpUtil.BASEURL + url)
                .tag(this)
                .isMultipart(true)
                .params(params)
                .addFileParams("file", list)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dismissDialog();
                        onHttpRequestResult(response, requestId);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        dismissDialog();
                        onHttpRequestErr(response, requestId);
                    }
                });
    }

    private void httpRequestByGetFinal(String url, HttpHeaders header, HttpParams params, final int requestId) {


        String finalUrl = HttpUtil.BASEURL + url;
        Log.e(TAG, finalUrl );
        OkGo.<String>get(finalUrl)
                .tag(this)
                .headers(header)
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dismissDialog();
                        onHttpRequestResult(response, requestId);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        dismissDialog();
                        onHttpRequestErr(response, requestId);
                    }
                });
    }

    protected void onHttpRequestResult(Response<String> response, int requestId) {

    }

    protected void onHttpRequestErr(Response<String> response, int id) {
        ToastUtil.showToast(context, getString(R.string.toast_net_failed));
    }


    protected void showProgressDialog(String msg) {
        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
                pDialog = null;
            }
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage(msg);
            pDialog.show();
        } catch (Exception e) {
            if (activity.getParent() != null) {
                activity = activity.getParent();
                pDialog = new ProgressDialog(activity);
                pDialog.setMessage(msg);
                pDialog.show();
            }
            e.printStackTrace();
        }
    }

    protected void dismissDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    @Override
    protected void onDestroy() {

        pDialog = null;
        AppManager.getAppManager().removeActivity(this);
        OkGo.getInstance().cancelTag(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    /**
     * 设置布局前的操作
     */
    protected void onBeforeSetContentLayout() {
    }

    /**
     * 加载页面layout
     */
    protected abstract void loadViewLayout();

    /**
     * 加载页面元素
     */
    protected abstract void findViewById();

    /**
     * 设置各种事件的监听器
     */
    protected abstract void setListener();

    /**
     * 业务逻辑处理，主要与后端交互
     */
    protected abstract void processLogic();

    protected SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    // 到登陆页面
    protected void toLoginInfo(int index) {
        Intent in = new Intent(context, LoginActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        in.putExtra("index", index);
        startActivity(in);
    }
}
