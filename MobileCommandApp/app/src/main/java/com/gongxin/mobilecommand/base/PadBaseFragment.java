/*
 * Copyright (c) 2016. Vv <envyfan@qq.com><http://www.v-sounds.com/>
 *
 * This file is part of AndroidReview (Android面试复习)
 *
 * AndroidReview is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  AndroidReview is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 * along with AndroidReview.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.gongxin.mobilecommand.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.global.Constants;
import com.gongxin.mobilecommand.ui.activity.login.LoginActivity;
import com.gongxin.mobilecommand.utils.HttpUtil;
import com.gongxin.mobilecommand.utils.SPUtil;
import com.gongxin.mobilecommand.utils.ToastUtil;
import com.gyf.immersionbar.components.ImmersionFragment;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;


public abstract class PadBaseFragment extends ImmersionFragment {

    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;
    protected boolean isCreate = true;
    protected ProgressDialog pDialog;
    protected PadBaseActivity mActivity;
    protected Context context;
    private View mRootView;


    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    protected void onAttachToContext(Context context) {
        this.mActivity = (PadBaseActivity) context;
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = initView(inflater, container);
        ButterKnife.bind(this, mRootView);//绑定到butterknife
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListener();
        initData();
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container);

    protected abstract void initListener();

    protected abstract void initData();

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
        header.put("token", SPUtil.get(context, "usertoken", "null") + "");
        httpRequestByGetFinal(url, header, params, requestId);
    }

    private void httpRequestByPostFinal(String url, HttpHeaders header, JSONObject params, final int requestId) {


        OkGo.<String>post(HttpUtil.BASEURL + url)
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

    private void httpRequestByGetFinal(String url, HttpHeaders header, HttpParams params, final int requestId) {


        OkGo.<String>get(HttpUtil.BASEURL + url)
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


    protected void onHttpRequestResult(Response<String> response, int requestId) {

    }

    protected void onHttpRequestErr(Response<String> response, int id) {
        ToastUtil.showToast(context, getString(R.string.toast_net_failed));
    }


    protected void showProgressDialog(String msg) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
            pDialog = null;
        }
        pDialog = new ProgressDialog(context);
        pDialog.setMessage(msg);
        pDialog.show();
    }


    protected void dismissDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCreate = false;
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    /**
     * 当Fragment可见时调用该方法
     */

    public void onVisible() {

    }

    /**
     * Fragment不可见时调用
     */
    public void onInvisible() {
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        pDialog = null;
        OkGo.getInstance().cancelTag(this);
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

    // 到登陆页面
    protected void toLoginInfo(int index) {
        Intent in = new Intent(context, LoginActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        in.putExtra("index", index);
        startActivity(in);
    }
}
