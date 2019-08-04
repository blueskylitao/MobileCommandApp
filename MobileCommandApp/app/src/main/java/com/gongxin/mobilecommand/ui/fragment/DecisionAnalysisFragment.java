package com.gongxin.mobilecommand.ui.fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.base.BaseFragment;

/**
 * 决策分析页面
 */
public class DecisionAnalysisFragment extends BaseFragment {

    public static String TAG = DecisionAnalysisFragment.class.getSimpleName();

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_decision_analysis, container, false);
        return rootView;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void initImmersionBar() {

    }
}
