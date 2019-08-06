package com.gongxin.mobilecommand.ui.fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.base.BaseFragment;
import com.gongxin.mobilecommand.base.PadBaseFragment;

/**
 * 首页仪表盘页面
 */
public class DashboardFragment extends PadBaseFragment {

    public static String TAG = DashboardFragment.class.getSimpleName();

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
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
