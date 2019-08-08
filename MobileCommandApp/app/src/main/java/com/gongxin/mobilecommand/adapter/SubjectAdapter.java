package com.gongxin.mobilecommand.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.domain.DecisionSubject;

import java.util.List;


/**
 *
 */

public class SubjectAdapter extends BaseQuickAdapter<DecisionSubject, BaseViewHolder> {

    public SubjectAdapter(int layoutResId, @Nullable List<DecisionSubject> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DecisionSubject item) {

        helper.setText(R.id.tv_title, item.getName());
        // helper.setText(R.id.iv_isattention, item.getCommentDetail());
        helper.setText(R.id.tv_detail, item.getDescription());
        helper.setText(R.id.tv_department, item.getDutyDept());
        helper.setText(R.id.tv_publish_time, item.getPublishTime());
        helper.setText(R.id.tv_update_time, item.getUpdateTime());
        helper.setText(R.id.tv_unit_name, item.getBuildUnit());
        helper.setText(R.id.tv_like_count, item.getApproveNum());
        helper.setText(R.id.tv_hot_count, item.getVisitNum());

        helper.addOnClickListener(R.id.iv_isattention);
        helper.addOnClickListener(R.id.ll_like);
        helper.addOnClickListener(R.id.ll_hot);
        helper.addOnClickListener(R.id.ll_comment);

    }
}
