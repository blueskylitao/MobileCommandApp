package com.gongxin.mobilecommand.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.gongxin.mobilecommand.R;

import org.jetbrains.annotations.NotNull;

/**
 * 首页仪表盘页面
 */
public class DashboardFragment extends Fragment {

    public static String TAG = DashboardFragment.class.getSimpleName();

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

}
