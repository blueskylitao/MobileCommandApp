package com.gongxin.mobilecommand.ui.fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.base.PadBaseFragment;
import com.gongxin.mobilecommand.domain.UrlMessageEvent;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 决策分析页面
 */
public class DecisionAnalysisFragment extends PadBaseFragment {

    public static String TAG = DecisionAnalysisFragment.class.getSimpleName();

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_decision_analysis, container, false);
        return rootView;
    }

    @Override
    protected void initListener() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void initImmersionBar() {

    }

    private void getSubjectDataById(int parentId, int requestId) {

        try {
            HttpParams httpParams = new HttpParams();
            httpParams.put("parentId", parentId);
            httpRequestByGet("/command/subject/getSubjectByCount", httpParams, requestId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onHttpRequestResult(Response<String> response, int requestId) {
        super.onHttpRequestResult(response, requestId);

    }

    @Override
    protected void onHttpRequestErr(Response<String> response, int id) {
        super.onHttpRequestErr(response, id);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(UrlMessageEvent messageEvent) {

    }
}
