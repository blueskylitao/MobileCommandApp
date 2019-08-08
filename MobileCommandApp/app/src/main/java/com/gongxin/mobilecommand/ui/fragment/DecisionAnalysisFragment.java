package com.gongxin.mobilecommand.ui.fragment;


import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.adapter.SubjectAdapter;
import com.gongxin.mobilecommand.base.PadBaseFragment;
import com.gongxin.mobilecommand.domain.DecisionSubject;
import com.gongxin.mobilecommand.domain.SubjectIdMessageEvent;
import com.gongxin.mobilecommand.utils.DensityUtil;
import com.gongxin.mobilecommand.utils.ToastUtil;
import com.gongxin.mobilecommand.view.GridSectionAverageGapItemDecoration;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 决策分析页面
 */
public class DecisionAnalysisFragment extends PadBaseFragment {

    public static String TAG = DecisionAnalysisFragment.class.getSimpleName();
    private SubjectAdapter subjectAdapter;
    private List<DecisionSubject> mlists = new ArrayList<DecisionSubject>();
    @BindView(R.id.rv_list)
    RecyclerView mRvList;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_decision_analysis, container, false);
        return rootView;
    }

    @Override
    protected void initListener() {
        EventBus.getDefault().register(this);
        mRvList.setLayoutManager(new GridLayoutManager(context, 5));
        mRvList.addItemDecoration(new GridSectionAverageGapItemDecoration(20, 10, 20, 15));
        subjectAdapter = new SubjectAdapter(R.layout.item_child_subject_layout, mlists);
        mRvList.setAdapter(subjectAdapter);

        subjectAdapter.setOnItemClickListener((adapter, view, position) -> {
            ToastUtil.showToast(context, "点击了条目");
        });

        subjectAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.iv_isattention:

                    break;
                case R.id.ll_like:
                    ToastUtil.showToast(context, "点击了 like");
                    break;
                case R.id.ll_hot:

                    break;
                case R.id.ll_comment:

                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void initData() {
        getCommonlyUsedSubject(1);
    }

    @Override
    public void initImmersionBar() {

    }

    // 获取常用专题
    private void getCommonlyUsedSubject(int requestId) {

        try {
            HttpParams httpParams = new HttpParams();
            httpParams.put("count", 20);
            httpRequestByGet("command/subject/getSubjectByCount", httpParams, requestId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 根据id查询子专题
    private void getChildSubjectById(int parentId, int requestId) {

        try {
            HttpParams httpParams = new HttpParams();
            httpParams.put("parentId", parentId);
            httpRequestByGet("command/subject/subjectTree", httpParams, requestId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onHttpRequestResult(Response<String> response, int requestId) {
        super.onHttpRequestResult(response, requestId);
        if (requestId == 1 || requestId == 2) {
            List<DecisionSubject> dataList = JSON.parseArray(response.body(), DecisionSubject.class);
            if (dataList.size() > 0) {
                mlists.clear();
                mlists.addAll(dataList);
            } else {
                TextView textView = new TextView(context);
                textView.setHeight(DensityUtil.getScreenHeightByDP(context));
                textView.setText("暂无数据");
                textView.setGravity(Gravity.CENTER);
                subjectAdapter.setEmptyView(textView);
            }
            subjectAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onHttpRequestErr(Response<String> response, int id) {
        super.onHttpRequestErr(response, id);
        TextView textView = new TextView(context);
        textView.setHeight(DensityUtil.getScreenHeightByDP(context));
        textView.setText("暂无网络连接");
        textView.setGravity(Gravity.CENTER);
        subjectAdapter.setEmptyView(textView);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(SubjectIdMessageEvent messageEvent) {
        int parentId = messageEvent.getParentId();
        if (messageEvent.getCommonlyUsed()) {
            mTvTitle.setText(messageEvent.getTitle());
            getCommonlyUsedSubject(1);
        } else {
            mTvTitle.setText(messageEvent.getTitle());
            getChildSubjectById(parentId, 2);
        }
    }
}
