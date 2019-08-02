package com.gongxin.mobilecommand.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.domain.McTargetMenuItem;

import java.util.List;

/**
 * Created by jiazhen on 2019-08-01.
 * Desc:
 */
public class McTargetAdapter extends BaseQuickAdapter<McTargetMenuItem, BaseViewHolder> {

    public McTargetAdapter(int layoutResId, @Nullable List<McTargetMenuItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, McTargetMenuItem item) {
        helper.setText(R.id.title,item.getName());
    }
}
