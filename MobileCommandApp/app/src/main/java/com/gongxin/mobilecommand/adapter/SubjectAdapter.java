package com.gongxin.mobilecommand.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.domain.DecisionSubject;

import java.util.List;


/**
 *z专题adapter
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
        switch (helper.getLayoutPosition() % 4) {
            case 0:
                helper.setImageResource(R.id.iv_logo, R.mipmap.zonghe_ic);
                helper.setBackgroundRes(R.id.ll_logo, R.drawable.logo_item_bg);
                break;
            case 1:
                helper.setImageResource(R.id.iv_logo, R.mipmap.zonghe_ic_two);
                helper.setBackgroundRes(R.id.ll_logo, R.drawable.logo_item_bg_two);
                break;
            case 2:
                helper.setImageResource(R.id.iv_logo, R.mipmap.zonghe_ic_three);
                helper.setBackgroundRes(R.id.ll_logo, R.drawable.logo_item_bg_three);
                break;
            case 3:
                helper.setImageResource(R.id.iv_logo, R.mipmap.zonghe_ic_four);
                helper.setBackgroundRes(R.id.ll_logo, R.drawable.logo_item_bg_four);
                break;
            default:
                break;

        }
        helper.addOnClickListener(R.id.iv_isattention);
        helper.addOnClickListener(R.id.ll_like);
        helper.addOnClickListener(R.id.ll_hot);
        helper.addOnClickListener(R.id.ll_comment);

    }
}
